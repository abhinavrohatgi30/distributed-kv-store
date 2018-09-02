package com.github.abhinavrohatgi30.dao;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public class WriteAheadLogDAO implements  DAO{


    private File baseFolder;

    private static final Logger logger = LogManager.getLogger(FileDAO.class);

    public WriteAheadLogDAO(String baseFolder) throws IOException {
        logger.info("Base Folder for Write Ahead Log---------->" +baseFolder);
        init(baseFolder);
    }

    private void init(String baseFolder) throws IOException{
        this.baseFolder = new File(baseFolder);
        boolean isFolderCreated = this.baseFolder.mkdirs();
        if(isFolderCreated)
            logger.debug(String.format("Folders were created for the path : %s ", baseFolder));
    }


    @Override
    public String read(String key) {
//        if(read(key) == null) {
//            try {
//                FileUtils.write(dictionaryFile, String.format("%s%s%s\n", key, SEPARATOR, value), Charset.defaultCharset(),true);
//                dictionary.put(key, value);
//            }catch (Exception e){
//                logger.error(String.format("Error writing the key-value pair to file -> %s : %s ", key,value));
//                return 0;
//            }
//            logger.info(String.format("key-value pair written to file -> %s : %s ", key,value));
//        }
        return null;
    }

    @Override
    public String write(String key, String value) {
        return null;
    }

    @Override
    public int delete(String key) {
        return 0;
    }
}
