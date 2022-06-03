package com.saintgobain.dsi.website4sg.core.web.rest;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.saintgobain.dsi.website4sg.core.domain.enumeration.ProjectTypeId;
import com.saintgobain.dsi.website4sg.core.exception.NotImplementedException;

/**
 * Utility class for testing REST controllers.
 */
@RunWith(SpringRunner.class)
public final class TestUtil {

    /** MediaType for JSON UTF8 */
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(
            MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    /**
     * Convert an object to JSON byte array.
     *
     * @param object the object to convert
     * @return the JSON byte array
     * @throws IOException
     */
    public static byte[] convertObjectToJsonBytes(Object object)
            throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        JavaTimeModule module = new JavaTimeModule();
        mapper.registerModule(module);
        return mapper.writeValueAsBytes(object);
    }

    /**
     * Create a byte array with a specific size filled with specified data.
     *
     * @param size the size of the byte array
     * @param data the data to put in the byte array
     * @return the JSON byte array
     */
    public static byte[] createByteArray(int size, String data) {
        byte[] byteArray = new byte[size];
        for (int i = 0; i < size; i++) {
            byteArray[i] = Byte.parseByte(data, 2);
        }
        return byteArray;
    }

    /**
     * Create a FormattingConversionService which use ISO date format, instead of the localized one.
     * 
     * @return the FormattingConversionService
     */
    public static FormattingConversionService createFormattingConversionService() {
        DefaultFormattingConversionService dfcs = new DefaultFormattingConversionService();
        DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
        registrar.setUseIsoFormat(true);
        registrar.registerFormatters(dfcs);
        return dfcs;
    }

    @Test
    public void projectTypeIdTest() throws NotImplementedException {
        ProjectTypeId projectTypeId = ProjectTypeId.getEnum("ddc");
        System.out.println(projectTypeId.getName());

        projectTypeId = ProjectTypeId.getEnum("w");
        System.out.println(projectTypeId.getName());
    }

    @Test
    public void streamSkipLimitTest() {
        List<Integer> numbers = new ArrayList<>();
        numbers.add(1);
        numbers.add(2);
        numbers.add(3);
        numbers.add(4);
        numbers.add(5);
        numbers.add(6);
        numbers.add(7);
        numbers.add(8);
        numbers.add(9);

        Stream<Integer> stream1 = numbers.stream();
        // Limit - return new stream of 3 elements
        System.out.println("--------Stream elements after limiting----------");
        stream1.limit(3).forEach((a) -> {
            System.out.println(a);
        });

        Stream<Integer> stream2 = numbers.stream();
        // Skip - return new stream of remaining elements
        // after skipping first 2 elements
        System.out.println("--------Stream elements after skipping----------");
        stream2.skip(2).forEach((a) -> {
            System.out.println(a);
        });

        Stream<Integer> stream3 = numbers.stream();
        // Limit - return new stream of 3 remaining elements
        System.out.println("--------Stream elements after skiping and limiting----------");
        stream3.skip(3).limit(3).forEach((a) -> {
            System.out.println(a);
        });

    }

    @Test
    public void forkJoinStreamTest() throws InterruptedException, ExecutionException {

        long firstNum = 1;
        long lastNum = 1_000;

        List<Long> aList = LongStream.rangeClosed(firstNum, lastNum).boxed()
                .collect(Collectors.toList());

        System.out.println("START FORK");
        ForkJoinPool customThreadPool = new ForkJoinPool(2);
        customThreadPool.execute(
                () -> aList.parallelStream().map(a -> {
                    System.out.println("TOTO");
                    return a;
                }).reduce(0L, Long::sum));

        System.out.println("END FORK");

    }

    @Test
    public void forkJoinParallelStreamTest() throws InterruptedException, ExecutionException {

        long firstNum = 1;
        long lastNum = 1_000;

        List<Long> aList = LongStream.rangeClosed(firstNum, lastNum).boxed()
                .collect(Collectors.toList());

        System.out.println("START PARA");
        aList.parallelStream().map(a -> {
            System.out.println("TITI");
            return a;
        }).reduce(0L, Long::sum);

        System.out.println("END PARA");

    }

}
