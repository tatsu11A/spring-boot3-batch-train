package com.example.sb3bt;

// Spring Boot アプリケーションを起動するためのクラス
import org.springframework.boot.SpringApplication;
// Spring Boot の自動構成とコンポーネントスキャンを有効にするアノテーション
import org.springframework.boot.autoconfigure.SpringBootApplication;
// アプリケーションコンテキストを操作するためのインターフェース
import org.springframework.context.ConfigurableApplicationContext;

// このアノテーションで、このクラスが Spring Boot アプリケーションのエントリポイントであることを示す
@SpringBootApplication
public class Sb3btApplication {

	public static void main(String[] args) {
		// Spring Boot アプリケーションを起動し、アプリケーションコンテキストを取得
		ConfigurableApplicationContext context = SpringApplication.run(Sb3btApplication.class, args);
		
		// アプリケーションを終了し、終了コード（0:成功, その他:異常）を取得
		int exitCode = SpringApplication.exit(context);

		// 終了コードを使って JVM を明示的に終了（バッチ処理などで成功／失敗を区別するため）
		System.exit(exitCode);	
	}
}
