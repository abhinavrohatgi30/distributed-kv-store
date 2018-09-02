package com.github.abhinavrohatgi30.dao;


import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.Null;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class FileDAO implements  DAO{

    private File baseFolder;
    private File dictionaryFile;
    private Map<String,String> dictionary;
    private static final String SEPARATOR = "^^";
    private static final String SPLIT_SEPARATOR = "\\^\\^";

    private static final Logger logger = LogManager.getLogger(FileDAO.class);

    public FileDAO(String baseFolder) throws IOException{
        logger.info("Base Folder for Data Store ---------->" +baseFolder);
        init(baseFolder);
    }

    private void init(String baseFolder) throws IOException{
        this.baseFolder = new File(baseFolder);
        boolean isFolderCreated = this.baseFolder.mkdirs();
        if(isFolderCreated)
            logger.debug(String.format("Folders were created for the path : %s ", baseFolder));
        this.dictionaryFile = new File(baseFolder,"dictionary.txt");
        boolean isFileCreated = this.dictionaryFile.createNewFile();
        if(isFileCreated)
            logger.debug(String.format("Data File was created for the path : %s ", dictionaryFile.getAbsolutePath()));
        readDictionaryFile();
    }

    private void readDictionaryFile() throws IOException{
        List<String> keyValues = FileUtils.readLines(this.dictionaryFile, Charset.defaultCharset());
        this.dictionary = new HashMap<>();
        for(String keyValue : keyValues){
            String[] split = keyValue.split(SPLIT_SEPARATOR);
            dictionary.put(split[0],split[1]);
        }
    }

    @Override
    public String read(String key) throws NullPointerException {
        return dictionary.get(key);
    }

    @Override
    public int write(String key, String value) {
        if(read(key) == null) {
            try {
                FileUtils.write(dictionaryFile, String.format("%s%s%s\n", key, SEPARATOR, value),Charset.defaultCharset(),true);
                dictionary.put(key, value);
            }catch (Exception e){
                logger.error(String.format("Error writing the key-value pair to file -> %s : %s ", key,value));
                return 0;
            }
            logger.info(String.format("key-value pair written to file -> %s : %s ", key,value));
        }
        return 1;
    }

    @Override
    public int delete(String key) {
        return 0;
    }
}
