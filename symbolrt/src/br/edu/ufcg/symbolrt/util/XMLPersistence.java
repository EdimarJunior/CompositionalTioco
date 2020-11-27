/*
 * SYmbolic Model-Based test case generation toOL for Real-Time systems (SYMBOLRT)
 * (C) Copyright 2010-2013 Federal University of Campina Grande (UFCG)
 * 
 * This file is part of SYMBOLRT.
 *
 * SYMBOLRT is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * SYMBOLRT is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with SYMBOLRT.  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 * REVISION HISTORY:
 * Author                           Date           Brief Description
 * -------------------------------- -------------- ------------------------------
 * Joeffison Silverio de Andrade    31/07/2012     Initial version
 * 
 */
package br.edu.ufcg.symbolrt.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;


/**
 * <code>XMLPersistence</code> Class.<br>
 * This class is used to load and save objects in XML files.
 * 
 * @author Joeffison Silverio de Andrade  ( <a href="mailto:joeffison.andrade@ccc.ufcg.edu.br">joeffison.andrade@ccc.ufcg.edu.br</a> )
 * 
 * @version 1.0
 * <br>
 * SYmbolic Model-Based test case generation toOL for Real-Time systems (SYMBOLRT)
 * <br>
 * (C) Copyright 2010-2013 Federal University of Campina Grande (UFCG)
 * <br>
 * <a href="https://sites.google.com/a/computacao.ufcg.edu.br/symbolrt">https://sites.google.com/a/computacao.ufcg.edu.br/symbolrt</a>
 */
public final class XMLPersistence implements PersistenceManager {

    /**
     * This instance is the unique instance in an execution.
     */
    private static XMLPersistence instance;
    
    /**
     * This {@link Object} is used to load and save XML files.
     */
    private XStream xstream;

    private XMLPersistence() {
        xstream = new XStream(new StaxDriver());
    }


    /**
     * Returns the unique instance of this class.
     * 
     * @return The unique instance of this class.
     */
    public static XMLPersistence getInstance() {
        if(instance == null) {
            instance = new XMLPersistence();
        }
        return instance;
    }

    /**
     * Reads the XML file and returns the {@link Object}.
     * 
     * @param fileName The name of the XML file with the object to restore.
     * @return The {@link Object} contained in the file.
     * @throws IOException If there's a problem in the file.
     */
    @Override
    public Object restore(String fileName) throws IOException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName));
        Object obj = xstream.fromXML(ois);
        ois.close();
        return obj;
    }

    /**
     * Saves the {@link Object} in a XML file.
     * 
     * @param object The {@link Object} that will be saved in a XML file.
     * @param fileName The name of the XML file where the {@link Object} will be saved.
     * @throws IOException If there's a problem in the file.
     */
    @Override
    public void store(Object object, String fileName) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName));
        xstream.toXML(object, oos);
        oos.flush();
        oos.close();
    }
    

}
