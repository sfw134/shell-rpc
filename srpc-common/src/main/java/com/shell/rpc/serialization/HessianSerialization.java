package com.shell.rpc.serialization;

import com.caucho.hessian.io.HessianSerializerInput;
import com.caucho.hessian.io.HessianSerializerOutput;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class HessianSerialization implements Serialization {


    @Override
    public <T> byte[] serialize(T obj) throws Exception {

        if (obj == null) {
            throw new NullPointerException();
        }

        byte[] results;
        HessianSerializerOutput hessianOutput;

        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            hessianOutput = new HessianSerializerOutput(os);
            hessianOutput.writeObject(obj);
            hessianOutput.flush();
            results = os.toByteArray();
        } catch (Exception e) {
            throw new SerializationException(e);
        }

        return results;
    }

    @Override
    public <T> T deserialize(byte[] data, Class<?> T) throws Exception {

        if (data == null) {
            throw new NullPointerException();
        }

        T result;
        try (ByteArrayInputStream is = new ByteArrayInputStream(data)) {
            HessianSerializerInput hessianInput = new HessianSerializerInput(is);
            result = (T) hessianInput.readObject(T);
        } catch (Exception e) {
            throw new SerializationException(e);
        }

        return result;
    }
}
