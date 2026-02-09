package spring.ai.example.spring_ai_demo;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class LionAiApplication {

	@Bean
	public CommandLineRunner runner(ChatClient.Builder builder, ToolCallbackProvider mcpToolProvider, ChatMemory chatMemory) {
		return args -> {
			var scanner = new Scanner(System.in);
			ChatClient chatClient = builder
					.defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
					.build();
			while(true) {

//				System.out.print("Prompt: ");
//				String prompt = scanner.nextLine().trim();
				String prompt = "Get the latest news from my confluence space (Cosmin Visan) in the last 10 minutes and give me answers as indicated," +
						"if nothing new was found just return empty";
				ResponseEntities response = chatClient
						.prompt(prompt)
						.system("You are a assistant that needs to check the latest news, releases, documentation and " +
								"see if the are new information that i should know about, when i specify to check a source, you will do that and be concise in response")
						.tools(new DateTimeTools())
						.toolCallbacks(mcpToolProvider)
						.call()
						.entity(ResponseEntities.class);
				System.out.println("Response: ");
				for (ResponseEntity respons : response.responses) {
					System.out.println("Title: " + respons.name);
					System.out.println("Description: " + respons.description);
					System.out.println("Link: " + respons.link);
					System.out.println("Important findings: " + respons.importantFindings);

					String content = chatClient.prompt("Create a Jira ticket on the Hackathon board with the following title '" + respons.name + "' " +
									"and the following description '" + respons.description + "'")
							.tools(new DateTimeTools())
							.toolCallbacks(mcpToolProvider)
							.call()
							.content();
				}

				if (!response.responses.isEmpty()) {
					chatClient.prompt("Create a windows notification with the title 'Created some issues that may interest you on Jira'")
							.tools(new DateTimeTools())
							.toolCallbacks(mcpToolProvider)
							.call()
							.content();
				}

				TimeUnit.MINUTES.sleep(5);
			}
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(LionAiApplication.class, args);
	}
}
