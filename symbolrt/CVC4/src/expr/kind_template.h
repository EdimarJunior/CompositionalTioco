/*********************                                                        */
/*! \file kind_template.h
 ** \verbatim
 ** Top contributors (to current version):
 **   Andres Noetzli, Morgan Deters, Dejan Jovanovic
 ** This file is part of the CVC4 project.
 ** Copyright (c) 2009-2020 by the authors listed in the file AUTHORS
 ** in the top-level source directory and their institutional affiliations.
 ** All rights reserved.  See the file COPYING in the top-level source
 ** directory for licensing information.\endverbatim
 **
 ** \brief Template for the Node kind header
 **
 ** Template for the Node kind header.
 **/

#include "cvc4_public.h"

#ifndef CVC4__KIND_H
#define CVC4__KIND_H

#include <iosfwd>

#include "base/exception.h"
#include "theory/theory_id.h"

namespace CVC4 {
namespace kind {

enum CVC4_PUBLIC Kind_t {
  UNDEFINED_KIND = -1, /**< undefined */
  NULL_EXPR, /**< Null kind */
${kind_decls}
  LAST_KIND /**< marks the upper-bound of this enumeration */

};/* enum Kind_t */

}/* CVC4::kind namespace */

// import Kind into the "CVC4" namespace but keep the individual kind
// constants under kind::
typedef ::CVC4::kind::Kind_t Kind;

namespace kind {

/**
 * Converts an kind to a string. Note: This function is also used in
 * `safe_print()`. Changing this functions name or signature will result in
 * `safe_print()` printing "<unsupported>" instead of the proper strings for
 * the enum values.
 *
 * @param k The kind
 * @return The name of the kind
 */
const char* toString(CVC4::Kind k);

/**
 * Writes a kind name to a stream.
 *
 * @param out The stream to write to
 * @param k The kind to write to the stream
 * @return The stream
 */
std::ostream& operator<<(std::ostream&, CVC4::Kind) CVC4_PUBLIC;

/** Returns true if the given kind is associative. This is used by ExprManager to
 * decide whether it's safe to modify big expressions by changing the grouping of
 * the arguments. */
/* TODO: This could be generated. */
bool isAssociative(::CVC4::Kind k) CVC4_PUBLIC;
std::string kindToString(::CVC4::Kind k) CVC4_PUBLIC;

struct KindHashFunction {
  inline size_t operator()(::CVC4::Kind k) const {
    return k;
  }
};/* struct KindHashFunction */

}/* CVC4::kind namespace */

/**
 * The enumeration for the built-in atomic types.
 */
enum CVC4_PUBLIC TypeConstant
{
  ${type_constant_list}
  LAST_TYPE
}; /* enum TypeConstant */

/**
 * We hash the constants with their values.
 */
struct TypeConstantHashFunction {
  inline size_t operator()(TypeConstant tc) const {
    return tc;
  }
};/* struct TypeConstantHashFunction */

std::ostream& operator<<(std::ostream& out, TypeConstant typeConstant);

namespace theory {

::CVC4::theory::TheoryId kindToTheoryId(::CVC4::Kind k) CVC4_PUBLIC;
::CVC4::theory::TheoryId typeConstantToTheoryId(
    ::CVC4::TypeConstant typeConstant) CVC4_PUBLIC;

}/* CVC4::theory namespace */
}/* CVC4 namespace */

#endif /* CVC4__KIND_H */
