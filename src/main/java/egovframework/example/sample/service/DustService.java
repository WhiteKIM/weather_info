package egovframework.example.sample.service;

import java.util.List;

public interface DustService {
	DustVO getDustByCity(Double x, Double y) throws Exception;
	List<DustVO> getDustBySido(String sido) throws Exception;
}
