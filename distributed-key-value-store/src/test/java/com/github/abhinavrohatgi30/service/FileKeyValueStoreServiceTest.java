package com.github.abhinavrohatgi30.service;

import com.github.abhinavrohatgi30.cluster.config.ClusterConfig;
import com.github.abhinavrohatgi30.dao.DAO;
import com.github.abhinavrohatgi30.routing.RequestRouter;
import com.github.abhinavrohatgi30.routing.RequestRouterFactory;
import com.github.abhinavrohatgi30.routing.ShardRequestRouter;
import com.github.abhinavrohatgi30.sharding.ShardGroupLocator;
import com.github.abhinavrohatgi30.util.ResponseMessage;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({RequestRouterFactory.class})
@PowerMockIgnore("javax.management.*")
public class FileKeyValueStoreServiceTest {

    @Mock
    private DAO dao;

    @Mock
    private ShardGroupLocator shardGroupLocator;

    private ClusterConfig clusterConfig;

    private FileKeyValueStoreService fileKeyValueStoreService;


    @Before
    public void init() throws IOException{
        MockitoAnnotations.initMocks(this);
        clusterConfig = new ClusterConfig("../cluster-conf.yaml");
        fileKeyValueStoreService = new FileKeyValueStoreService(clusterConfig,dao,shardGroupLocator);
    }

    @Test
    public void getValue_shouldInvokeDaoAndReturnAValue_WhenKeyIsPresentInTheSameShardGroup(){
        String key = "test_key_1";
        String expectedValue = "test_value_1";
        Mockito.when(shardGroupLocator.locate(Mockito.any())).thenReturn("1");
        Mockito.when(dao.read(Mockito.any())).thenReturn(expectedValue);
        String value = fileKeyValueStoreService.getValue(key);
        Mockito.verify(dao).read(key);
        assertEquals(value,expectedValue);
    }

    @Test
    public void getValue_shouldInvokeDaoAndReturnAMessageSayingTheKeyIsNotAvailable_WhenDAOThrowsANullPointerException(){
        String key = "test_key_1";
        String expectedValue = "test_value_1";
        Mockito.when(shardGroupLocator.locate(Mockito.any())).thenReturn("1");
        Mockito.when(dao.read(Mockito.any())).thenThrow(NullPointerException.class);
        String value = fileKeyValueStoreService.getValue(key);
        Mockito.verify(dao).read(key);
        assertEquals(value, ResponseMessage.KEY_NOT_AVAILABLE);
    }

    @Test
    public void getValue_shouldInvokeRoutGetRequestMethodOfTheRequestRouted_WhenKeyIsPresentInADifferentShardGroup(){
        String key = "test_key_1";
        String expectedValue = "test_value_1";
        mockStatic(RequestRouterFactory.class);
        Mockito.when(shardGroupLocator.locate(Mockito.any())).thenReturn("2");
        RequestRouter requestRouter = Mockito.mock(RequestRouter.class);
        Mockito.when(requestRouter.routeGetRequest(Mockito.any())).thenReturn(expectedValue);
        Mockito.when(RequestRouterFactory.getRequestRouter(Mockito.any(),Mockito.any())).thenReturn(requestRouter);
        String value = fileKeyValueStoreService.getValue(key);
        verifyStatic(Mockito.times(1));
        RequestRouterFactory.getRequestRouter("2",clusterConfig);
        Mockito.verify(requestRouter).routeGetRequest(Mockito.any());
        assertEquals(expectedValue,value);
    }

    @Test
    public void indexKeyValue_shouldInvokeDaoAndWriteTheKeyValue_WhenKeyValueIsToBePutInSameShardGroupAndIsRoutedFromADifferentNode(){
        String key = "test_key_1";
        String expectedValue = "test_value_1";
        Mockito.when(shardGroupLocator.locate(Mockito.any())).thenReturn("1");
        Mockito.when(dao.write(Mockito.anyString(),Mockito.anyString())).thenReturn(ResponseMessage.WRITE_OPERATION_SUCCESS);
        String value = fileKeyValueStoreService.indexKeyValue(key,expectedValue,true);
        Mockito.verify(dao).write(key,expectedValue);
        assertEquals(value,ResponseMessage.WRITE_OPERATION_SUCCESS);
    }

    @Test
    public void indexKeyValue_shouldInvokeDaoAndReturnWriteFailed_WhenKeyValueIsToBePutInSameShardGroupAndIsRoutedFromADifferentNodeAndDAOThrowsAnException(){
        String key = "test_key_1";
        String expectedValue = "test_value_1";
        Mockito.when(shardGroupLocator.locate(Mockito.any())).thenReturn("1");
        Mockito.when(dao.write(Mockito.anyString(),Mockito.anyString())).thenThrow(Exception.class);
        String value = fileKeyValueStoreService.indexKeyValue(key,expectedValue,true);
        Mockito.verify(dao).write(key,expectedValue);
        assertEquals(value,ResponseMessage.WRITE_OPERATION_FAILED);
    }

    @Test
    public void indexKeyValue_shouldInvokeRequestRoutedAndReturnAMessageWithDetailedNodeDescription_WhenKeyValueIsToBePutInSameShardGroupAndIsNotRouted() throws JSONException{
        String key = "test_key_1";
        String expectedValue = "test_value_1";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("http://localhost:8900",ResponseMessage.WRITE_OPERATION_SUCCESS);
        jsonObject.put("http://localhost:8910",ResponseMessage.WRITE_OPERATION_SUCCESS);
        mockStatic(RequestRouterFactory.class);
        ShardRequestRouter requestRouter = Mockito.mock(ShardRequestRouter.class);
        Mockito.when(shardGroupLocator.locate(Mockito.any())).thenReturn("2");
        Mockito.when(requestRouter.routePutRequest(Mockito.any())).thenReturn(jsonObject.toString());
        Mockito.when(RequestRouterFactory.getRequestRouter(Mockito.any(),Mockito.any())).thenReturn(requestRouter);
        String value = fileKeyValueStoreService.indexKeyValue(key,expectedValue,false);
        verifyStatic(Mockito.times(1));
        RequestRouterFactory.getRequestRouter("2",clusterConfig);
        Mockito.verify(requestRouter).routePutRequest(Mockito.any());
        assertEquals(value,jsonObject.toString());
    }
}
