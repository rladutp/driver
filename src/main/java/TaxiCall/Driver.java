package TaxiCall;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;

@Entity
@Table(name="Driver_table")
public class Driver {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long driverId;
    private String driverStatus;
    private Long orderId;
    private String status;
    private String location;

    //기사 등록
    @PostPersist
    public void onPostPersist(){
        System.out.println("##### 1 #####");
            if(this.getDriverStatus().equals("registered")){
                System.out.println("##### status is registered #####");
                TaxiCall.external.Hr hr = new TaxiCall.external.Hr();

                hr.setDriverId(this.getDriverId());
                hr.setDriverStatus(this.getDriverStatus());
                System.out.println("hr id : "+hr.getDriverId());
                System.out.println("hr status : "+hr.getDriverStatus());
                DriverApplication.applicationContext.getBean(TaxiCall.external.HrService.class)
                        .inputDr(hr);
                System.out.println("1234");

                //view에 넣기 위해 카프카로 보낸다.

                Registered registered = new Registered();
                registered.setDriverId(hr.getDriverId());
                registered.setDriverStatus(hr.getDriverStatus());
                BeanUtils.copyProperties(this, registered);
                registered.publishAfterCommit();
                System.out.println("5678");

            }

    }
//driver가 자신의 상태를 업데이트를 한다 아직 미개발
/*    @PrePersist
    public void onPrePersist() {

        System.out.println("START");
        if (this.getStatus().equals("Agreed")) {
            System.out.println("seq1");
            OrderAgreed orderAgreed = new OrderAgreed();

            orderAgreed.setOrderId(this.getOrderId());
            orderAgreed.setStatus("Agreed");
            orderAgreed.setDriverId(this.getDriverId());

            BeanUtils.copyProperties(this, orderAgreed);
            orderAgreed.publishAfterCommit();

        } else if (this.getStatus().equals("Declined")) {
            System.out.println("seq2");
            OrderDeclined orderDeclined = new OrderDeclined();

            orderDeclined.setOrderId(this.getOrderId());
            orderDeclined.setStatus("Declined");
            orderDeclined.setDriverId(this.getDriverId());

            BeanUtils.copyProperties(this, orderDeclined);
            orderDeclined.publishAfterCommit();
        }
    }
*/

    @PostUpdate
    public void onPostUpdate(){
        System.out.println("START2");

        if(this.getDriverStatus().equals("retired")){
            System.out.println("##### status is retired #####");
            Retired retired = new Retired();

            retired.setDriverId(this.getDriverId());
            retired.setDriverStatus(this.getDriverStatus());

            BeanUtils.copyProperties(this, retired);
            retired.publishAfterCommit();

        }

        if(null != this.getStatus()&&!"".equals(this.getStatus())){
            if (this.getStatus().equals("Agreed")) {
                System.out.println("seq1");
                OrderAgreed orderAgreed = new OrderAgreed();

                orderAgreed.setOrderId(this.getOrderId());
                orderAgreed.setStatus("Agreed");
                orderAgreed.setDriverId(this.getDriverId());

                BeanUtils.copyProperties(this, orderAgreed);
                orderAgreed.publishAfterCommit();

            } else if (this.getStatus().equals("Declined")) {
                System.out.println("seq2");
                OrderDeclined orderDeclined = new OrderDeclined();

                orderDeclined.setOrderId(this.getOrderId());
                orderDeclined.setStatus("Declined");
                orderDeclined.setDriverId(this.getDriverId());

                BeanUtils.copyProperties(this, orderDeclined);
                orderDeclined.publishAfterCommit();
            }
        }

    }


    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }


    public String getDriverStatus() {
        return driverStatus;
    }

    public void setDriverStatus(String driverStatus) {
        this.driverStatus = driverStatus;
    }

}
