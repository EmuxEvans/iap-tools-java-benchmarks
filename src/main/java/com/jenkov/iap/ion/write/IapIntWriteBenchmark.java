package com.jenkov.iap.ion.write;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by jjenkov on 07-11-2015.
 */
public class IapIntWriteBenchmark {

    public static class SingleFieldPojo {
        public int field0 = 1;
    }

    public static class MultiFieldPojo {
        public int field0   = 1;
        public int field1   = 12;
        public int field2   = 123;
        public int field3   = 1234;
        public int field4   = 12345;
        public int fieldxx5 = 123456;         //xx to make the field name longer than 7 chars - to force longer key field (instead of compact key field)
        public int fieldxx6 = 1234567;
        public int fieldxx7 = 12345678;
        public int fieldxx8 = 123456789;
        public int fieldxx9 = 1234567890;
    }



    @State(Scope.Thread)
    public static class IapState {
        SingleFieldPojo singleFieldPojo = new SingleFieldPojo();
        MultiFieldPojo  multiFieldPojo = new MultiFieldPojo();

        IonObjectWriter writerSingle = new IonObjectWriter(SingleFieldPojo.class);
        IonObjectWriter writerMulti  = new IonObjectWriter(MultiFieldPojo.class);

        byte[]    dest   = new byte[10 * 1024];
    }

    @State(Scope.Thread)
    public static class JacksonState {
        SingleFieldPojo singleFieldPojo = new SingleFieldPojo();
        MultiFieldPojo multiFieldPojo = new MultiFieldPojo();

        ObjectMapper objectMapper = new ObjectMapper();

        ByteArrayOutputStream out = new ByteArrayOutputStream(10 * 1024);

        @Setup(Level.Invocation)
        public void doSetup() {
            out.reset();
        }

    }


    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public Object iapWriteSingle(IapState state, Blackhole blackhole) {

        state.writerSingle.writeObject(state.singleFieldPojo, 2, state.dest, 0);

        blackhole.consume(state.dest);
        return state.dest;
    }


    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public Object iapWriteMulti(IapState state, Blackhole blackhole) {

        state.writerMulti.writeObject(state.multiFieldPojo, 2, state.dest, 0);

        blackhole.consume(state.dest);
        return state.dest;
    }



    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public Object jacksonWriteSingle(JacksonState state, Blackhole blackhole) {

        try {
            state.objectMapper.writeValue(state.out, state.singleFieldPojo);
        } catch (IOException e) {
            e.printStackTrace();
        }

        blackhole.consume(state.out);
        return state.out;
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public Object jacksonWriteMulti(JacksonState state, Blackhole blackhole) {

        try {
            state.objectMapper.writeValue(state.out, state.multiFieldPojo);
        } catch (IOException e) {
            e.printStackTrace();
        }

        blackhole.consume(state.out);
        return state.out;
    }






}
