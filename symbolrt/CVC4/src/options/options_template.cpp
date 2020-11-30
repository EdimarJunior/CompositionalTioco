/*********************                                                        */
/*! \file options_template.cpp
 ** \verbatim
 ** Top contributors (to current version):
 **   Morgan Deters, Tim King, Andrew Reynolds
 ** This file is part of the CVC4 project.
 ** Copyright (c) 2009-2020 by the authors listed in the file AUTHORS
 ** in the top-level source directory and their institutional affiliations.
 ** All rights reserved.  See the file COPYING in the top-level source
 ** directory for licensing information.\endverbatim
 **
 ** \brief Contains code for handling command-line options.
 **
 ** Contains code for handling command-line options
 **/

#if !defined(_BSD_SOURCE) && defined(__MINGW32__) && !defined(__MINGW64__)
// force use of optreset; mingw32 croaks on argv-switching otherwise
#  include "cvc4autoconfig.h"
#  define _BSD_SOURCE
#  undef HAVE_DECL_OPTRESET
#  define HAVE_DECL_OPTRESET 1
#  define CVC4_IS_NOT_REALLY_BSD
#endif /* !_BSD_SOURCE && __MINGW32__ && !__MINGW64__ */

#ifdef __MINGW64__
extern int optreset;
#endif /* __MINGW64__ */

#include <getopt.h>

// clean up
#ifdef CVC4_IS_NOT_REALLY_BSD
#  undef _BSD_SOURCE
#endif /* CVC4_IS_NOT_REALLY_BSD */

#include <unistd.h>
#include <string.h>
#include <time.h>

#include <cstdio>
#include <cstdlib>
#include <cstring>
#include <iomanip>
#include <new>
#include <string>
#include <sstream>
#include <limits>

#include "base/check.h"
#include "base/exception.h"
#include "base/output.h"
#include "options/didyoumean.h"
#include "options/language.h"
#include "options/options_handler.h"
#include "options/options_listener.h"

${headers_module}$


#include "options/options_holder.h"
#include "cvc4autoconfig.h"
#include "options/base_handlers.h"

${headers_handler}$


using namespace CVC4;
using namespace CVC4::options;

namespace CVC4 {

thread_local Options* Options::s_current = NULL;

/**
 * This is a default handler for options of built-in C++ type.  This
 * template is really just a helper for the handleOption() template,
 * below.  Variants of this template handle numeric and non-numeric,
 * integral and non-integral, signed and unsigned C++ types.
 * handleOption() makes sure to instantiate the right one.
 *
 * This implements default behavior when e.g. an option is
 * unsigned but the user specifies a negative argument; etc.
 */
template <class T, bool is_numeric, bool is_integer>
struct OptionHandler {
  static T handle(std::string option, std::string optionarg);
};/* struct OptionHandler<> */

/** Variant for integral C++ types */
template <class T>
struct OptionHandler<T, true, true> {
  static bool stringToInt(T& t, const std::string& str) {
    std::istringstream ss(str);
    ss >> t;
    char tmp;
    return !(ss.fail() || ss.get(tmp));
  }

  static bool containsMinus(const std::string& str) {
    return str.find('-') != std::string::npos;
  }

  static T handle(const std::string& option, const std::string& optionarg) {
    try {
      T i;
      bool success = stringToInt(i, optionarg);

      if(!success){
        throw OptionException(option + ": failed to parse "+ optionarg +
                              " as an integer of the appropriate type.");
      }

      // Depending in the platform unsigned numbers with '-' signs may parse.
      // Reject these by looking for any minus if it is not signed.
      if( (! std::numeric_limits<T>::is_signed) && containsMinus(optionarg) ) {
        // unsigned type but user gave negative argument
        throw OptionException(option + " requires a nonnegative argument");
      } else if(i < std::numeric_limits<T>::min()) {
        // negative overflow for type
        std::stringstream ss;
        ss << option << " requires an argument >= "
           << std::numeric_limits<T>::min();
        throw OptionException(ss.str());
      } else if(i > std::numeric_limits<T>::max()) {
        // positive overflow for type
        std::stringstream ss;
        ss << option << " requires an argument <= "
           << std::numeric_limits<T>::max();
        throw OptionException(ss.str());
      }

      return i;

      // if(std::numeric_limits<T>::is_signed) {
      //   return T(i.getLong());
      // } else {
      //   return T(i.getUnsignedLong());
      // }
    } catch(std::invalid_argument&) {
      // user gave something other than an integer
      throw OptionException(option + " requires an integer argument");
    }
  }
};/* struct OptionHandler<T, true, true> */

/** Variant for numeric but non-integral C++ types */
template <class T>
struct OptionHandler<T, true, false> {
  static T handle(std::string option, std::string optionarg) {
    std::stringstream in(optionarg);
    long double r;
    in >> r;
    if(! in.eof()) {
      // we didn't consume the whole string (junk at end)
      throw OptionException(option + " requires a numeric argument");
    }

    if(! std::numeric_limits<T>::is_signed && r < 0.0) {
      // unsigned type but user gave negative value
      throw OptionException(option + " requires a nonnegative argument");
    } else if(r < -std::numeric_limits<T>::max()) {
      // negative overflow for type
      std::stringstream ss;
      ss << option << " requires an argument >= "
         << -std::numeric_limits<T>::max();
      throw OptionException(ss.str());
    } else if(r > std::numeric_limits<T>::max()) {
      // positive overflow for type
      std::stringstream ss;
      ss << option << " requires an argument <= "
         << std::numeric_limits<T>::max();
      throw OptionException(ss.str());
    }

    return T(r);
  }
};/* struct OptionHandler<T, true, false> */

/** Variant for non-numeric C++ types */
template <class T>
struct OptionHandler<T, false, false> {
  static T handle(std::string option, std::string optionarg) {
    T::unsupported_handleOption_call___please_write_me;
    // The above line causes a compiler error if this version of the template
    // is ever instantiated (meaning that a specialization is missing).  So
    // don't worry about the segfault in the next line, the "return" is only
    // there to keep the compiler from giving additional, distracting errors
    // and warnings.
    return *(T*)0;
  }
};/* struct OptionHandler<T, false, false> */

/** Handle an option of type T in the default way. */
template <class T>
T handleOption(std::string option, std::string optionarg) {
  return OptionHandler<T, std::numeric_limits<T>::is_specialized, std::numeric_limits<T>::is_integer>::handle(option, optionarg);
}

/** Handle an option of type std::string in the default way. */
template <>
std::string handleOption<std::string>(std::string option, std::string optionarg) {
  return optionarg;
}

/**
 * Run handler, and any user-given predicates, for option T.
 * If a user specifies a :handler or :predicates, it overrides this.
 */
template <class T>
typename T::type runHandlerAndPredicates(T, std::string option, std::string optionarg, options::OptionsHandler* handler) {
  // By default, parse the option argument in a way appropriate for its type.
  // E.g., for "unsigned int" options, ensure that the provided argument is
  // a nonnegative integer that fits in the unsigned int type.

  return handleOption<typename T::type>(option, optionarg);
}

template <class T>
void runBoolPredicates(T, std::string option, bool b, options::OptionsHandler* handler) {
  // By default, nothing to do for bool.  Users add things with
  // :predicate in options files to provide custom checking routines
  // that can throw exceptions.
}

Options::Options(OptionsListener* ol)
    : d_holder(new options::OptionsHolder()),
      d_handler(new options::OptionsHandler(this)),
      d_olisten(ol)
{}

Options::~Options() {
  delete d_handler;
  delete d_holder;
}

void Options::copyValues(const Options& options){
  if(this != &options) {
    delete d_holder;
    d_holder = new options::OptionsHolder(*options.d_holder);
  }
}

std::string Options::formatThreadOptionException(const std::string& option) {
  std::stringstream ss;
  ss << "can't understand option `" << option
     << "': expected something like --threadN=\"--option1 --option2\","
     << " where N is a nonnegative integer";
  return ss.str();
}

void Options::setListener(OptionsListener* ol) { d_olisten = ol; }

${custom_handlers}$

#if defined(CVC4_MUZZLED) || defined(CVC4_COMPETITION_MODE)
#  define DO_SEMANTIC_CHECKS_BY_DEFAULT false
#else /* CVC4_MUZZLED || CVC4_COMPETITION_MODE */
#  define DO_SEMANTIC_CHECKS_BY_DEFAULT true
#endif /* CVC4_MUZZLED || CVC4_COMPETITION_MODE */

options::OptionsHolder::OptionsHolder() :
  ${module_defaults}$
{
}


static const std::string mostCommonOptionsDescription = "\
Most commonly-used CVC4 options:\n"
${help_common}$;


static const std::string optionsDescription = mostCommonOptionsDescription + "\n\
\n\
Additional CVC4 options:\n"
${help_others}$;


static const std::string optionsFootnote = "\n\
[*] Each of these options has a --no-OPTIONNAME variant, which reverses the\n\
    sense of the option.\n\
";

static const std::string languageDescription =
    "\
Languages currently supported as arguments to the -L / --lang option:\n\
  auto                           attempt to automatically determine language\n\
  cvc4 | presentation | pl       CVC4 presentation language\n\
  smt | smtlib | smt2 |\n\
  smt2.0 | smtlib2 | smtlib2.0   SMT-LIB format 2.0\n\
  smt2.5 | smtlib2.5             SMT-LIB format 2.5\n\
  smt2.6 | smtlib2.6             SMT-LIB format 2.6 with support for the strings standard\n\
  tptp                           TPTP format (cnf, fof and tff)\n\
  sygus | sygus2                 SyGuS version 2.0\n\
\n\
Languages currently supported as arguments to the --output-lang option:\n\
  auto                           match output language to input language\n\
  cvc4 | presentation | pl       CVC4 presentation language\n\
  cvc3                           CVC3 presentation language\n\
  smt | smtlib | smt2 |\n\
  smt2.0 | smtlib2.0 | smtlib2   SMT-LIB format 2.0\n\
  smt2.5 | smtlib2.5             SMT-LIB format 2.5\n\
  smt2.6 | smtlib2.6             SMT-LIB format 2.6 with support for the strings standard\n\
  tptp                           TPTP format\n\
  ast                            internal format (simple syntax trees)\n\
";

std::string Options::getDescription() const {
  return optionsDescription;
}

void Options::printUsage(const std::string msg, std::ostream& out) {
  out << msg << optionsDescription << std::endl
      << optionsFootnote << std::endl << std::flush;
}

void Options::printShortUsage(const std::string msg, std::ostream& out) {
  out << msg << mostCommonOptionsDescription << std::endl
      << optionsFootnote << std::endl
      << "For full usage, please use --help."
      << std::endl << std::endl << std::flush;
}

void Options::printLanguageHelp(std::ostream& out) {
  out << languageDescription << std::flush;
}

/**
 * This is a table of long options.  By policy, each short option
 * should have an equivalent long option (but the reverse isn't the
 * case), so this table should thus contain all command-line options.
 *
 * Each option in this array has four elements:
 *
 * 1. the long option string
 * 2. argument behavior for the option:
 *    no_argument - no argument permitted
 *    required_argument - an argument is expected
 *    optional_argument - an argument is permitted but not required
 * 3. this is a pointer to an int which is set to the 4th entry of the
 *    array if the option is present; or NULL, in which case
 *    getopt_long() returns the 4th entry
 * 4. the return value for getopt_long() when this long option (or the
 *    value to set the 3rd entry to; see #3)
 *
 * If you add something here, you should add it in src/main/usage.h
 * also, to document it.
 *
 * If you add something that has a short option equivalent, you should
 * add it to the getopt_long() call in parseOptions().
 */
static struct option cmdlineOptions[] = {
  ${cmdline_options}$
  { NULL, no_argument, NULL, '\0' }
};/* cmdlineOptions */

namespace options {

/** Set a given Options* as "current" just for a particular scope. */
class OptionsGuard {
  Options** d_field;
  Options* d_old;
public:
  OptionsGuard(Options** field, Options* opts) :
    d_field(field),
    d_old(*field) {
    *field = opts;
  }
  ~OptionsGuard() {
    *d_field = d_old;
  }
};/* class OptionsGuard */

}/* CVC4::options namespace */

/**
 * Parse argc/argv and put the result into a CVC4::Options.
 * The return value is what's left of the command line (that is, the
 * non-option arguments).
 *
 * Throws OptionException on failures.
 */
std::vector<std::string> Options::parseOptions(Options* options,
                                               int argc,
                                               char* argv[])
{
  Assert(options != NULL);
  Assert(argv != NULL);

  options::OptionsGuard guard(&s_current, options);

  const char *progName = argv[0];

  // To debug options parsing, you may prefer to simply uncomment this
  // and recompile. Debug flags have not been parsed yet so these have
  // not been set.
  //DebugChannel.on("options");

  Debug("options") << "Options::parseOptions == " << options << std::endl;
  Debug("options") << "argv == " << argv << std::endl;

  // Find the base name of the program.
  const char *x = strrchr(progName, '/');
  if(x != NULL) {
    progName = x + 1;
  }
  options->d_holder->binary_name = std::string(progName);

  std::vector<std::string> nonoptions;
  parseOptionsRecursive(options, argc, argv, &nonoptions);
  if(Debug.isOn("options")){
    for(std::vector<std::string>::const_iterator i = nonoptions.begin(),
          iend = nonoptions.end(); i != iend; ++i){
      Debug("options") << "nonoptions " << *i << std::endl;
    }
  }

  return nonoptions;
}

void Options::parseOptionsRecursive(Options* options,
                                    int argc,
                                    char* argv[],
                                    std::vector<std::string>* nonoptions)
{

  if(Debug.isOn("options")) {
    Debug("options") << "starting a new parseOptionsRecursive with "
                     << argc << " arguments" << std::endl;
    for( int i = 0; i < argc ; i++ ){
      Assert(argv[i] != NULL);
      Debug("options") << "  argv[" << i << "] = " << argv[i] << std::endl;
    }
  }

  // Having this synonym simplifies the generation code in mkoptions.
  options::OptionsHandler* handler = options->d_handler;

  // Reset getopt(), in the case of multiple calls to parseOptions().
  // This can be = 1 in newer GNU getopt, but older (< 2007) require = 0.
  optind = 0;
#if HAVE_DECL_OPTRESET
  optreset = 1; // on BSD getopt() (e.g. Mac OS), might need this
#endif /* HAVE_DECL_OPTRESET */

  // We must parse the binary name, which is manually ignored below. Setting
  // this to 1 leads to incorrect behavior on some platforms.
  int main_optind = 0;
  int old_optind;


  while(true) { // Repeat Forever

    optopt = 0;
    std::string option, optionarg;

    optind = main_optind;
    old_optind = main_optind;

    // If we encounter an element that is not at zero and does not start
    // with a "-", this is a non-option. We consume this element as a
    // non-option.
    if (main_optind > 0 && main_optind < argc &&
        argv[main_optind][0] != '-') {
      Debug("options") << "enqueueing " << argv[main_optind]
                       << " as a non-option." << std::endl;
      nonoptions->push_back(argv[main_optind]);
      ++main_optind;
      continue;
    }


    Debug("options") << "[ before, main_optind == " << main_optind << " ]"
                     << std::endl;
    Debug("options") << "[ before, optind == " << optind << " ]" << std::endl;
    Debug("options") << "[ argc == " << argc << ", argv == " << argv << " ]"
                     << std::endl;
    int c = getopt_long(argc, argv,
                        "+:${options_short}$",
                        cmdlineOptions, NULL);

    main_optind = optind;

    Debug("options") << "[ got " << int(c) << " (" << char(c) << ") ]"
                     << "[ next option will be at pos: " << optind << " ]"
                     << std::endl;

    // The initial getopt_long call should always determine that argv[0]
    // is not an option and returns -1. We always manually advance beyond
    // this element.
    if ( old_optind == 0  && c == -1 ) {
      Assert(main_optind > 0);
      continue;
    }

    if ( c == -1 ) {
      if(Debug.isOn("options")) {
        Debug("options") << "done with option parsing" << std::endl;
        for(int index = optind; index < argc; ++index) {
          Debug("options") << "remaining " << argv[index] << std::endl;
        }
      }
      break;
    }

    option = argv[old_optind == 0 ? 1 : old_optind];
    optionarg = (optarg == NULL) ? "" : optarg;

    Debug("preemptGetopt") << "processing option " << c
                           << " (`" << char(c) << "'), " << option << std::endl;

    switch(c) {
${options_handler}$


    case ':':
      // This can be a long or short option, and the way to get at the
      // name of it is different.
      throw OptionException(std::string("option `") + option +
                            "' missing its required argument");

    case '?':
    default:
      throw OptionException(std::string("can't understand option `") + option +
                            "'" + suggestCommandLineOptions(option));
    }
  }

  Debug("options") << "got " << nonoptions->size()
                   << " non-option arguments." << std::endl;
}

std::string Options::suggestCommandLineOptions(const std::string& optionName)
{
  DidYouMean didYouMean;

  const char* opt;
  for(size_t i = 0; (opt = cmdlineOptions[i].name) != NULL; ++i) {
    didYouMean.addWord(std::string("--") + cmdlineOptions[i].name);
  }

  return didYouMean.getMatchAsString(optionName.substr(0, optionName.find('=')));
}

static const char* smtOptions[] = {
  ${options_smt}$
  NULL
};/* smtOptions[] */

std::vector<std::string> Options::suggestSmtOptions(
    const std::string& optionName)
{
  std::vector<std::string> suggestions;

  const char* opt;
  for(size_t i = 0; (opt = smtOptions[i]) != NULL; ++i) {
    if(std::strstr(opt, optionName.c_str()) != NULL) {
      suggestions.push_back(opt);
    }
  }

  return suggestions;
}

std::vector<std::vector<std::string> > Options::getOptions() const
{
  std::vector< std::vector<std::string> > opts;

  ${options_getoptions}$


  return opts;
}

void Options::setOption(const std::string& key, const std::string& optionarg)
{
  Trace("options") << "setOption(" << key << ", " << optionarg << ")"
                   << std::endl;
  // first update this object
  setOptionInternal(key, optionarg);
  // then, notify the provided listener
  if (d_olisten != nullptr)
  {
    d_olisten->notifySetOption(key);
  }
}

void Options::setOptionInternal(const std::string& key,
                                const std::string& optionarg)
{
  options::OptionsHandler* handler = d_handler;
  Options* options = this;
  ${setoption_handlers}$
  throw UnrecognizedOptionException(key);
}

std::string Options::getOption(const std::string& key) const
{
  Trace("options") << "Options::getOption(" << key << ")" << std::endl;
  ${getoption_handlers}$

  throw UnrecognizedOptionException(key);
}

#undef USE_EARLY_TYPE_CHECKING_BY_DEFAULT
#undef DO_SEMANTIC_CHECKS_BY_DEFAULT

}  // namespace CVC4
