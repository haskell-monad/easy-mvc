package easy.framework.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import easy.framework.mvc.model.Result;
import easy.framework.mvc.model.View;

/**
 * Created by limengyu on 2017/9/22.
 */
public class JsonUtils {
	private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);
	private static final ObjectMapper mapper = new ObjectMapper();

	public static String toJson(Object object) {
		try {
			String json = mapper.writeValueAsString(object);
			return json;
		} catch (JsonProcessingException e) {
			throw new RuntimeException("json解析失败", e);
		}
	}
	public static <T> T toBean(String json, Class<T> clazz) {
		try {
            return mapper.readValue(json, clazz);
		} catch (IOException e) {
			throw new RuntimeException("json解析失败", e);
		}
	}
	public static <T> List<T> toBeanList(String json, Class<T> clazz) {
		JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, clazz);
		try {
            return mapper.readValue(json, javaType);
		} catch (IOException e) {
			throw new RuntimeException("json解析失败", e);
		}
	}
	public static void main(String[] args) {
		Result result = new Result();
		result.setCode(100);
		result.setData(new View("/path", new HashMap<String, Object>()));
		result.setMsg("success");
		String json = JsonUtils.toJson(result);
		System.out.println("json:" + json);
        System.out.println("=======================================");
        result = JsonUtils.toBean(json, Result.class);
		System.out.println("json:" + JsonUtils.toJson(result));
        System.out.println("=======================================");
        List<Result> list = new ArrayList<>();
        list.add(result);
        list.add(result);
        json = JsonUtils.toJson(list);
        System.out.println("json:" + json);
        System.out.println("=======================================");
        List<Result> results = JsonUtils.toBeanList(json, Result.class);
        System.out.println("json:" + JsonUtils.toJson(results));
	}
}
