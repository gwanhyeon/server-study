package com.kgh.serverstudy;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients // Feign Client를 사용할 것임을 알려줍니다.
@SpringBootTest
class ServerStudyApplicationTests {

    @Test
    void contextLoads() {
    }

}
