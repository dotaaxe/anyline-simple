package org.anyline.simple.rabbitmq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"org.anyline"})
@SpringBootApplication
public class RabbitmqApplication {


	public static void main(String[] args) throws Exception{
		SpringApplication application = new SpringApplication(RabbitmqApplication.class);

		ConfigurableApplicationContext context = application.run(args);
		//context.getBean(ProducerSimple.class).send();
		//context.getBean(ConsumerSimple.class).start();


/*
		context.getBean(ProducerFanout.class).send();
		context.getBean(ConsumerFanoutMail1.class).start();
		context.getBean(ConsumerFanoutMail2.class).start();
		context.getBean(ConsumerFanoutSMS.class).start();
		*/
/*

		context.getBean(ProducerRouting.class).send();
		context.getBean(ConsumerRoutingMail.class).start();
		context.getBean(ConsumerRoutingSMS.class).start();
*/
/*
		context.getBean(ProducerTopic.class).send();
		context.getBean(ConsumerTopicMail.class).start();
		context.getBean(ConsumerTopicSMS.class).start();
		*/

		context.getBean(ProducerHeader.class).send();
		context.getBean(ConsumerHeaderMail.class).start();
		context.getBean(ConsumerHeaderSMS.class).start();
	}

}
