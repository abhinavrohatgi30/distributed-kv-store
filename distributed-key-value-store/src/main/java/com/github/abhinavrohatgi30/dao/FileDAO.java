package com.github.abhinavrohatgi30.dao;


import org.apache.commons.io.FileUtils;
import org.springframework.context.annotation.Configuration;

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

    public FileDAO(String baseFolder) throws IOException{
        init(baseFolder);
    }

    private void init(String baseFolder) throws IOException{
        this.baseFolder = new File(baseFolder);
        this.baseFolder.mkdir();
        this.dictionaryFile = new File(baseFolder,"dictionary.txt");
        this.dictionaryFile.createNewFile();
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
    public String read(String key) {
        String value = null;
        try {
            value = dictionary.get(key);
        }catch (Exception e){
        }
        return value;
    }

    @Override
    public int write(String key, String value) {
        if(read(key) == null) {
            try {
                FileUtils.write(dictionaryFile, String.format("%s%s%s\n", key, SEPARATOR, value),Charset.defaultCharset(),true);
                dictionary.put(key, value);
            }catch (Exception e){
                return 0;
            }
        }
        return 1;
    }

    @Override
    public int delete(String key) {
        return 0;
    }
}
