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
 * Wilkerson de Lucena Andrade      04/09/2012     Refactoring
 * 
 */
package br.edu.ufcg.symbolrt.util;

import java.io.*;


/**
 * <code>PersistenceManager</code> Class.<br>
 * This interface describes operations in order to save objects in a file and load objects from files.
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
public interface PersistenceManager {

    /**
     * Reads the file and returns the {@link Object}.
     * 
     * @param fileName The name of the file with the object to restore.
     * @return The {@link Object} contained in the file.
     * @throws IOException If there's a problem in the file.
     */
    public Object restore(String fileName) throws IOException;

    /**
     * Saves the {@link Object} in a file.
     * 
     * @param object The {@link Object} that will be saved in a file.
     * @param fileName The name of the file where the {@link Object} will be saved.
     * @throws IOException If there's a problem in the file.
     */
    public void store(Object object, String fileName) throws IOException;
    
}
