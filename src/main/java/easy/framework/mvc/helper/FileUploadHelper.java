package easy.framework.mvc.helper;

import java.io.*;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import easy.framework.helper.ConfigHelper;
import easy.framework.mvc.model.FileModel;
import easy.framework.mvc.model.RequestParamModel;
import easy.framework.utils.JsonUtils;

/**
 * @author limengyu
 * @create 2017/09/25
 */
public class FileUploadHelper {
	private final static Logger logger = LoggerFactory.getLogger(FileUploadHelper.class);
	private static ServletFileUpload fileUpload;

	public static void init() {
		String repository = System.getProperty("java.io.tmpdir");
		logger.debug("文件上传repository: {}", repository);
		FileItemFactory factory = new DiskFileItemFactory(DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD, new File(repository));
		fileUpload = new ServletFileUpload(factory);
		// 页面请求传递信息最大值
		fileUpload.setSizeMax(ConfigHelper.getFileUploadMaxSize());
		// 单个上传文件信息最大值
		fileUpload.setFileSizeMax(ConfigHelper.getFileUploadSingleFileMaxSize());
	}
	public static boolean isMultipart(HttpServletRequest request) {
		return ServletFileUpload.isMultipartContent(request);
	}
	public static RequestParamModel parseFormParam(HttpServletRequest req) {
		RequestParamModel paramModel = new RequestParamModel();
		boolean isMultipart = isMultipart(req);
		if (!isMultipart) {
			return paramModel;
		}
		Map<String, List<String>> formFieldMap = new HashMap<>(16);
		List<FileModel> fileList = new ArrayList<>();
		// 开始解析请求信息
		List<FileItem> items;
		try {
			items = fileUpload.parseRequest(req);
		} catch (FileUploadException e) {
			throw new RuntimeException("文件上传失败",e);
		}
		// 对所有请求信息进行判断
		Iterator<FileItem> iterator = items.iterator();
		List<String> list;
		while (iterator.hasNext()) {
			FileItem fileItem = iterator.next();
			if (fileItem.isFormField()) {
				if (formFieldMap.containsKey(fileItem.getFieldName())) {
					list = formFieldMap.get(fileItem.getFieldName());
				} else {
					list = new ArrayList<>();
				}
				list.add(fileItem.getString());
				formFieldMap.put(fileItem.getFieldName(), list);
			} else {
				String realFileName = getRealFileName(fileItem);
				String tempFileName = getTempFileName(realFileName);
				FileModel uploadModel = new FileModel();
				uploadModel.setFileRealName(realFileName);
				uploadModel.setFileTempName(tempFileName);
				uploadModel.setFileSize(fileItem.getSize());
				uploadModel.setFileSuffix(getFileSuffix(uploadModel.getFileRealName()));
				try {
					uploadModel.setInputStream(fileItem.getInputStream());
				} catch (IOException e) {
					throw new RuntimeException("获取文件数据流失败",e);
				}
				fileList.add(uploadModel);
			}
		}
		paramModel.setFileList(fileList);
		paramModel.setFormFileMap(formFieldMap);
		logger.debug("获取到表单参数信息: {},文件上传数量：{}", JsonUtils.toJson(formFieldMap), fileList.size());
		return paramModel;
	}
	public static String getRealFileName(FileItem fileItem) {
		if (fileItem != null && StringUtils.isNotBlank(fileItem.getName())) {
			String fileName = fileItem.getName();
			int index = fileName.lastIndexOf("\\");
			return fileName.substring(index + 1);
		}
		throw new RuntimeException("获取上传文件名称异常");
	}
	public static String getFileSuffix(String fileName) {
		if (StringUtils.isBlank(fileName)) {
			return null;
		}
		return fileName.substring(fileName.lastIndexOf("."), fileName.length());
	}
	public static String getTempFileName(String fileName) {
		String date = DateFormatUtils.format(new Date(), "yyyyMMddHHmmSS");
		String tempFileName = fileName.substring(0, fileName.lastIndexOf(".")) + date + "" + randomStr() + getFileSuffix(fileName);
		logger.debug("文件老名称，新名称: {}\t{}", fileName, tempFileName);
		return tempFileName;
	}
	/**
	 * 获取5位随机数
	 * @return
	 */
	public static String randomStr() {
		Random random = new Random();
		return Integer.toString((int) (random.nextDouble() * (99999 - 10000 + 1)) + 10000);
	}
	public static void fileUpload(FileModel uploadModel) {
		File targetFile = new File(ConfigHelper.getFileUploadDirectory(), uploadModel.getFileTempName());
		InputStream inputStream = uploadModel.getInputStream();
		OutputStream outputStream = null;
		try {
			FileUtils.forceMkdir(targetFile);
			outputStream = new BufferedOutputStream(new FileOutputStream(targetFile));
			byte[] buffer = new byte[1024];
			int length;
			while ((length = inputStream.read(buffer, 0, buffer.length)) != -1) {
				outputStream.write(buffer, 0, length);
			}
			outputStream.flush();
		} catch (Exception e) {
			logger.error("targetFile: {}", targetFile);
			throw new RuntimeException("文件上传失败", e);
		} finally {
			try {
				outputStream.close();
				inputStream.close();
			} catch (IOException e) {
				logger.error("文件流关闭异常", e);
			}
		}
	}
	public static void fileUploadBatch(List<FileModel> list) {
		if (list != null && list.size() > 0) {
			for (FileModel uploadModel : list) {
				fileUpload(uploadModel);
			}
		}
	}
}
