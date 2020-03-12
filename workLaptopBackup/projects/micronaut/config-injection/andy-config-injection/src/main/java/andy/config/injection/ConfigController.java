package andy.config.injection;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Value;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;

@Controller("/config")
public class ConfigController {
    private static final Logger LOG = LoggerFactory.getLogger(ConfigController.class);

    @Property(name = "micronaut.team.name")
    private String teamP;
    
    @Value("${micronaut.team.name:crewe}")
    private String teamV;
    
    public ConfigController() {
    }

    @Get("/")
    public List<String> list() {

        out("getting list ...");

        List<String> l = new ArrayList<String>();
        l.add("one");
        l.add("two");
        return l;
    }

    @Get("/{itemId}")
    public String find(String itemId) {
        out("finding [" + itemId + "] ...");
        return "four";
    }

    @Post("/")
    public String save(String item) {
        out("saving [" + item + "] ...");
        return"done";
    }
    
    private void out(final String msg) {
        LOG.info(this.toString() + "---" + msg);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ConfigController [teamP=" + teamP + ", teamV=" + teamV + "]";
    }
    
    

}