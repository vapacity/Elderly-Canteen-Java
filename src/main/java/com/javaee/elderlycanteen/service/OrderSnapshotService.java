package com.javaee.elderlycanteen.service;

import com.javaee.elderlycanteen.exception.ServiceException;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.util.Base64;

/**
 * 离线订单快照恢复。
 *
 * SEC-LAB-A3：对请求中的 Base64 数据直接 ObjectInputStream.readObject()。
 * resolveClass 只拦了 Runtime/ProcessBuilder，黑名单不完整。
 */
@Service
public class OrderSnapshotService {

    public Object restore(String snapshot) throws Exception {
        if (snapshot == null || snapshot.isEmpty()) {
            throw new ServiceException("snapshot is empty");
        }

        byte[] bytes = Base64.getDecoder().decode(snapshot);
        try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes)) {
            @Override
            protected Class<?> resolveClass(ObjectStreamClass desc)
                    throws IOException, ClassNotFoundException {
                String name = desc.getName();
                if (name.startsWith("java.lang.Runtime") || name.startsWith("java.lang.ProcessBuilder")) {
                    throw new InvalidClassException("blocked class: " + name);
                }
                return super.resolveClass(desc);
            }
        }) {
            return in.readObject();
        }
    }
}
