package org.example.result;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Schema(description = "返回参数")
@Data
public class Result<T> {
    @Schema(description = "响应状态码")
    private Integer code;
    @Schema(description = "错误信息")
    private String message;
    @Schema(description = "响应数据")
    private T data;
    public static <E> Result<E> success(E data){
        return new Result<>(0,"操作成功",data);
    }

    public static Result success(){
        return new Result<>(0,"操作成功",null);
    }

    public static Result error(String message){
        return new Result<>(1,message,null);
    }
}
