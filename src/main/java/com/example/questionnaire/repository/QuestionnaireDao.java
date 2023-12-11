package com.example.questionnaire.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.questionnaire.entity.Questionnaire;
import com.example.questionnaire.vo.QnQuVo;

@Repository
public interface QuestionnaireDao extends JpaRepository<Questionnaire, Integer>{

	//取得最新一筆資料，撈出全部資料後倒序，取第一筆
//	public Questionnaire findTopByOrederByIdDesc();
	
	public List<Questionnaire> findByIdIn(List<Integer> idList);
	
	public List<Questionnaire> findByTitleContainingAndStartDateGreaterThanEqualAndEndDateLessThanEqual(String title, LocalDate startDate, LocalDate endDate);

	public List<Questionnaire> findByTitleContainingAndStartDateGreaterThanEqualAndEndDateLessThanEqualAndPublishedTrue(String title, LocalDate startDate, LocalDate endDate);

	@Modifying			//insert update delete
	@Transactional		//要加
	//可以直接寫sql語法							欄位名稱
	@Query(value = "insert into questionnaire (title, description, is_published, start_date, end_date) "
					// value 填下面@Param的名稱										
			+ " values (:title, :description, :isPublished, :startDate, :endDate)", nativeQuery = true)
	public int insert( //
			@Param("title") String title, //
			@Param("description") String description, //
			@Param("isPublished") boolean isPublished, //
			@Param("startDate")LocalDate startDate, //
			@Param("endDate") LocalDate endDate);
	
	
	//簡易寫法
	@Modifying		
	@Transactional		
	@Query(value = "insert into questionnaire (title, description, is_published, start_date, end_date) "							
			+ " values (?1, ?2, ?3, ?4, ?5)", nativeQuery = true)
	public int insertData( //
			String title, //
			String description, //
			boolean isPublished, //
			LocalDate startDate, //
			LocalDate endDate);
	
	//update
	@Modifying		
	@Transactional	
	@Query(value = "update questionnaire set title = :title, description = :description"
			+ " where id = :id", nativeQuery = true)
	public int update(
			@Param("id")int id, //
			@Param("title")String title, //
			@Param("description")String description);
	
	
	// 不寫 nativeQuery = true  等同於 , nativeQuery = false
	// 語法中表的名稱要變成 entity 的 class 名稱: 欄位名稱要變成屬性名稱
	// clearAutomatically = true:清除持久化上下文，即清除暫存資料
	@Modifying(clearAutomatically = true)	
	@Transactional	
	@Query(value = "update Questionnaire set title = :title, description = :description, startDate = :startDate"
			+ " where id = :id") 
	public int updateData(
			@Param("id")int id, //
			@Param("title")String title, //
			@Param("description")String description, //
			@Param("startDate") LocalDate startDate);

	//=============================================================
	//select	撈個別資料 最好用nativeQuery = false
	//						操作entity,需有對應的建構方法
	@Query(value = "select * from questionnaire "
			+ " where start_date > :startDate", nativeQuery = true)
	public  List<Questionnaire> findByStartDate(@Param("startDate") LocalDate startDate);
	
	//nativeQuery = false					entity內要有對應建構方法
	@Query(value = "select new Questionnaire(id, title, description, published, startDate,endDate) from Questionnaire "
			+ " where startDate > :startDate")
	public  List<Questionnaire> findByStartDate1(@Param("startDate") LocalDate startDate);
	
	//nativeQuery = false					欄位篩選，entity內要有對應建構方法
	@Query(value = "select new Questionnaire(id, title,  published) from Questionnaire "
			+ " where startDate >= :startDate")
	public  List<Questionnaire> findByStartDate2(@Param("startDate") LocalDate startDate);
	
	//					別名:qu                 as可省略  效果同1
	@Query(value = "select qu from Questionnaire as qu "
	//									篩選條件用or 或 and 增加
			+ " where startDate > :startDate or published = :isPublished")
	public  List<Questionnaire> findByStartDate3(
			@Param("startDate") LocalDate startDate, //
			@Param("isPublished") boolean published);
	
	//order by 
	@Query(value = "select qu from Questionnaire as qu "
			+ " where startDate > :startDate or published = :isPublished order by id desc")
	public  List<Questionnaire> findByStartDate4(
			@Param("startDate") LocalDate startDate, //
			@Param("isPublished") boolean published);
	
	//order by + limit  limit只能用 nativeQuery = true
	//limit要放在語法的最後
		@Query(value = "select * from questionnaire as qu "
				+ " where start_date > :startDate or is_published = :isPublished order by id desc limit :num", nativeQuery = true)
		public  List<Questionnaire> findByStartDate5(
				@Param("startDate") LocalDate startDate, //
				@Param("isPublished") boolean published, //
				@Param("num") int limitNum);
		
		
		@Query(value = "select * from questionnaire "
				+ " limit :startIndex, :limitNum", nativeQuery = true)
		public List<Questionnaire> findWithLimitAndStartIndex(
				@Param("startIndex")int startIndex, //
				@Param("limitNum")int limitNum);

		
		//like  error的話 %:title% 用 concat連接 : ('%', :title, '%')
		@Query(value = "select * from questionnaire "
				+ "where title like %:title%", nativeQuery = true)
		public List<Questionnaire> searchTitleLike(@Param("title")String title);
		
		//regexp 只能用在 nativeQuery = true
		@Query(value = "select * from questionnaire "
				+ "where title regexp :title", nativeQuery = true)
		public List<Questionnaire> searchTitleLike2(@Param("title")String title);
		
		
		//regexp or  有錯 用下面的
		@Query(value = "select * from questionnaire "
				+ "where description regexp :keyword1|:keyword2", nativeQuery = true)
		public List<Questionnaire> searchDescriptionContaining(
				@Param("keyword1")String keyword1, //
				@Param("keyword2")String keyword2);
		
		@Query(value = "select * from questionnaire "
				+ "where description regexp concat(:keyword1, '|', :keyword2)", nativeQuery = true)
		public List<Questionnaire> searchDescriptionContaining2(
				@Param("keyword1")String keyword1, //
				@Param("keyword2")String keyword2);
				
		//==============================================
		//join
		@Query("select new com.example.questionnaire.vo.QnQuVo("
				+ "qn.id, qn.title, qn.description, qn.published, qn.startDate, qn.endDate,"
				+ " q.quId, q.qTitle, q.optionType, q.necessary, q.option)"
				+ " from Questionnaire as qn join Question as q on qn.id = q.qnId")
		public List<QnQuVo> selectJoinQnQu();
		
		
		@Query("select new com.example.questionnaire.vo.QnQuVo("
				+ "qn.id, qn.title, qn.description, qn.published, qn.startDate, qn.endDate,"
				+ " q.quId, q.qTitle, q.optionType, q.necessary, q.option)"
				+ " from Questionnaire as qn join Question as q on qn.id = q.qnId "
				+ " where qn.title like %:title% and qn.startDate >= :startDate and qn.endDate <= :endDate")
		public List<QnQuVo> selectFuzzy(
				@Param("title")String title, //
				@Param("startDate")LocalDate startDate, //
				@Param("endDate")LocalDate endDate);
}
