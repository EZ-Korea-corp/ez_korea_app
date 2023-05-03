package com.ezkorea.hybrid_app.app.util;

import lombok.Getter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.http.HttpStatus;

public class ResponseData {
    public static <T> ApiResult<T> success(T response, String message) {
        return new ApiResult<>(true, message, response, null);
    }

    public static ApiResult<?> error(Throwable throwable, String message, HttpStatus status) {
        return new ApiResult<>(false, message, null, new ResponseData.ApiError(throwable, status));
    }

    public static ApiResult<?> error(String message, HttpStatus status) {
        return new ApiResult<>(false, null, null, new ResponseData.ApiError(message, status));
    }

    public static class ApiResult<T> {
        @Getter
        private final boolean success;
        @Getter
        private final String message;
        @Getter
        private final T response;
        @Getter
        private final ResponseData.ApiError error;

        private ApiResult(boolean success, String message, T response, ResponseData.ApiError error) {
            this.success = success;
            this.message = message;
            this.response = response;
            this.error = error;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                    .append("success", success)
                    .append("message", message)
                    .append("response", response)
                    .append("error", error)
                    .toString();
        }
    }


    public static class ApiError {
        @Getter
        private final String message;

        @Getter
        private final int status;

        ApiError(Throwable throwable, HttpStatus status) {
            this(throwable.getMessage(), status);
        }

        ApiError(String message, HttpStatus status) {
            this.message = message;
            this.status = status.value();
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                    .append("message", message)
                    .append("status", status)
                    .toString();
        }
    }
}
