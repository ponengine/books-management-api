package books_management.api.dto.common;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.MDC;

import java.time.LocalDateTime;

@Getter
@Setter
public class BaseResponse<T> {
        private String transactionId;
        private LocalDateTime transactionDate;
        private boolean status;
        private T data;
        private String error;

        public BaseResponse(T data,boolean status,String error) {
            String traceId = MDC.get("traceId");
            this.transactionId = traceId;
            this.transactionDate = LocalDateTime.now()  ;
            this.status= status;
            this.data = data;
            this.error = error;
        }


        public BaseResponse() {

        }

    @Override
    public String toString() {
        return "BaseResponse{" +
                "transactionId='" + transactionId + '\'' +
                ", transactionDate=" + transactionDate +
                ", data=" + data +
                ", error='" + error + '\'' +
                '}';
    }
}
