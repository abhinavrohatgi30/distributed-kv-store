package com.github.abhinavrohatgi30.dao;

import com.github.abhinavrohatgi30.util.ResponseMessage;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FileDAOTest {

    String testFileStorePath = "./test/fileDAO/";
    String fileStorePathWithData = "./test_data/fileDAO/";


    @Test
    public void constructor_shouldInitializeTheDictionaryWithValuesInTheDataStoreFile() throws IOException{
        FileDAO fileDAO = new FileDAO(fileStorePathWithData);
        assertEquals(fileDAO.read("test_key_1"),"test_data_1");
    }

    @Test
    public void constructor_shouldCreateTheRelevantDirectoriesAndFileIfNotPresent() throws IOException{
        FileDAO fileDAO = new FileDAO(testFileStorePath+"/data");
        File testFolder = new File(testFileStorePath);
        File testDataFile = new File(testFileStorePath + "/data/dictionary.txt");
        assertTrue(testFolder.exists());
        assertTrue(testDataFile.exists());
        testDataFile.delete();
        testFolder.delete();
    }

    @Test
    public void read_shouldReturnTheValueCorrespondingToTheKeySpecified_WhenAExistingKeyIsQueried() throws Exception{
        FileDAO fileDAO = new FileDAO(fileStorePathWithData);
        assertEquals(fileDAO.read("test_key_1"),"test_data_1");
    }

    @Test
    public void write_shouldWriteTheKeyValuePairToTheInMemoryMapAndDataFile_WhenAKeyValuePairIsSpecified() throws IOException{
        FileDAO fileDAO = new FileDAO(testFileStorePath+"/data");
        fileDAO.write("test_key_2","test_data_2");
        File testFolder = new File(testFileStorePath);
        File testDataFile = new File(testFileStorePath + "/data/dictionary.txt");
        assertEquals(fileDAO.read("test_key_2"),"test_data_2");
        List<String> kvPairs =  FileUtils.readLines(testDataFile, Charset.defaultCharset());
        kvPairs.get(0).equals("test_key_2^^test_data_2");
        testDataFile.delete();
        testFolder.delete();
    }

    @Test
    public void write_shouldReturnAKeyAlreadyPresentResponse_WhenSameKeyValuePairWrittenTwice() throws IOException{
        FileDAO fileDAO = new FileDAO(testFileStorePath+"/data");
        fileDAO.write("test_key_2","test_data_2");
        File testFolder = new File(testFileStorePath);
        File testDataFile = new File(testFileStorePath + "/data/dictionary.txt");
        String response = fileDAO.write("test_key_2","test_data_2");
        assertEquals(ResponseMessage.KEY_ALREADY_PRESENT,response);
        testDataFile.delete();
        testFolder.delete();
    }

    @Test
    public void write_shouldUpdateAKeyAlreadyPresent_WhenSameKeyButDifferentValueIsSpecified() throws IOException{
        FileDAO fileDAO = new FileDAO(testFileStorePath+"/data");
        fileDAO.write("test_key_2","test_data_2");
        File testFolder = new File(testFileStorePath);
        File testDataFile = new File(testFileStorePath + "/data/dictionary.txt");
        String response = fileDAO.write("test_key_2","test_data_3");
        assertEquals(fileDAO.read("test_key_2"),"test_data_3");
        testDataFile.delete();
        testFolder.delete();
    }


}
