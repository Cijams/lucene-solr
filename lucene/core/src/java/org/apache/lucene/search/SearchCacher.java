/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.lucene.search;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class SearchCacher {

  private String _query;
  private String _index;
  private File _indexCacheFile = null;
  
  
  public SearchCacher() {
    this("", "");
  }
  
  /**
   * Initialize the SearchCacher. On creation, the SearchCacher will check for a
   * cached file in the specified index directory. 
   * @param index Index directory 
   * @param query Query string used in the naming of the cached file
   */
  public SearchCacher(String index, String query) {
    if(!isNullOrEmpty(index) && !isNullOrEmpty(query)) {
        _index = index;
        _query = query;
        
        File indexDir = new File(_index);
        String[] extensions = new String[] { "txt" };
        try {          
          Collection<File> files = FileUtils.listFiles(indexDir, extensions, true);
          
          if(files != null && !files.isEmpty()) {
            List<File> fileList = new ArrayList<File>(files);
            _indexCacheFile = fileList.get(0);
          }
        } catch (Exception e) {
          System.out.println("Error occurred searching for cache file");
        }
    }
  }
  
  /**
   * Utility method to determine whether a string is empty or null
   * @param s Value to check if null or empty
   * @return True if the string is empty. False Otherwise
   */
  private boolean isNullOrEmpty(String s) {
    return s == null || s == "";
  }
  
  /**
   * Get the cached query file
   * @return Cached file. Returns null if the file doesn't exists.
   */
  private File getQueryCacheFile() {
    if(!isNullOrEmpty(_query)) {
      int queryHashCode = _query.hashCode();
      String fileName = "";
      
      if(queryHashCode < 0) {
        fileName = "n";
      }
      
      fileName += Math.abs(queryHashCode) + ".txt";
      
      if(!isNullOrEmpty(fileName)) {
        File baseDir = new File(_index);
        File cacheFile = new File(baseDir, fileName);
        return cacheFile;
      }
    }
    return null;
  }
  
  /**
   * Checks for the existence of a cached file.
   * @return True if cached file exists. False otherwise.
   */
  public boolean cacheFileExists() {
    return _indexCacheFile != null && _indexCacheFile.exists();
  }
  
  /**
   * Checks whether the provided query string matches the cached results found in the provided
   * index directory
   * @return True if queries match. False otherwise
   */
  public boolean queryMatchesCache() {
    if(!isNullOrEmpty(_query) && cacheFileExists()) {
        File queryCacheFile = getQueryCacheFile();
        if(queryCacheFile != null && queryCacheFile.exists()) {
          return queryCacheFile.getName().equals(_indexCacheFile.getName());
        }
    }
    return false;
  }
  
  /**
   * Get the cached results. 
   * @return Cached TopDocs. Returns null if no cached results available.
   */
  public TopDocs getCacheResults() {
    File queryCacheFile = getQueryCacheFile();
    if(queryCacheFile != null && queryCacheFile.exists()) {
      FileInputStream fs;
      try {
        fs = new FileInputStream(queryCacheFile);
        ObjectInputStream os = new ObjectInputStream(fs);
        
        TopDocs docs = (TopDocs) os.readObject();
        os.close();
        fs.close();
        
        return docs;
      } catch (Exception e) {
        if(e != null) {
          System.out.println("Error occurred: " + e.getMessage());
        }
      }
    }
    return null;
  }
  
  /**
   * Cache results
   * @param results Results to cache
   */
  public void CreateCacheFile(TopDocs results) {
    File queryCacheFile = getQueryCacheFile();
    if(queryCacheFile != null && results != null) {
      if(queryCacheFile.exists()) {
        queryCacheFile.delete();
      }
      
      if(_indexCacheFile != null && _indexCacheFile.exists()) {
        _indexCacheFile.delete();
      }
      
      try {
        queryCacheFile.createNewFile();
        
        FileOutputStream fs = new FileOutputStream(queryCacheFile);
        ObjectOutputStream os = new ObjectOutputStream(fs);
        
        os.writeObject(results);
        
        os.close();
        fs.close();
        
      } catch (Exception e) {
        if (e != null) {
          System.out.println("Error occurred: " + e.getMessage());
        }
      }
    }
  }
}
