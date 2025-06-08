package com.example.sb3bt.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// このクラスはSpringの設定クラスとして機能します
@Configuration
public class BatchConfiguration {

    // BatchExitCodeGeneratorというBeanをSpringコンテナに登録するメソッド
    @Bean
    BatchExitCodeGenerator exitCodeGenerator() {
        // BatchExitCodeGeneratorのインスタンスを生成して返す
        // これにより、バッチ処理の終了コードをカスタマイズする機能を提供する
        return new BatchExitCodeGenerator();
    }
}