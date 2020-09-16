
package TaxiCall.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

@FeignClient(name="HR", url= "${api.url.hr}") //url="http://HR:8080")
public interface HrService {

    @RequestMapping(method= RequestMethod.GET, path="/hrs/inputData")
    public void inputDr(@RequestBody Hr param);

}