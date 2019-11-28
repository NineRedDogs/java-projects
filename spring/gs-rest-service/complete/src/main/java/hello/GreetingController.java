package hello;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

    private static final String templateX = "XYZ---Hello, %s!";
    private static final String template = "Hello, %s!";
    private static final String templateError = "**ERROR** , %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/")
    public Greeting greetingRoot(@RequestParam(value="name", defaultValue="XXXX-World") String name) {
        return new Greeting(counter.incrementAndGet(),
                            String.format(templateX, name));
    }
    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return new Greeting(counter.incrementAndGet(),
                            String.format(template, name));
    }
    /*@RequestMapping("/error")
    public Greeting error(@RequestParam(value="name", defaultValue="jim") String name) {
        return new Greeting(counter.incrementAndGet(),
                            String.format(templateError, name));
    }*/
}
