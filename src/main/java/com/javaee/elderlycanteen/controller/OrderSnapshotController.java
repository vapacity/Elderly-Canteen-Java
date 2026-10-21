package com.javaee.elderlycanteen.controller;

import com.javaee.elderlycanteen.service.OrderSnapshotService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 离线订单快照恢复接口。
 *
 * SEC-LAB-A3：snapshot 由请求体进入反序列化 sink。
 */
@RestController
@RequestMapping("/orderInfos")
public class OrderSnapshotController {

    private final OrderSnapshotService orderSnapshotService;

    public OrderSnapshotController(OrderSnapshotService orderSnapshotService) {
        this.orderSnapshotService = orderSnapshotService;
    }

    @PostMapping("/snapshot/restore")
    public ResponseEntity<Map<String, Object>> restore(@RequestBody Map<String, String> body) throws Exception {
        Object restored = orderSnapshotService.restore(body.get("snapshot"));
        return ResponseEntity.ok(Map.of(
                "class", restored.getClass().getName()
        ));
    }
}
