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

	//���o�̷s�@����ơA���X������ƫ�˧ǡA���Ĥ@��
//	public Questionnaire findTopByOrederByIdDesc();
	
	public List<Questionnaire> findByIdIn(List<Integer> idList);
	
	public List<Questionnaire> findByTitleContainingAndStartDateGreaterThanEqualAndEndDateLessThanEqual(String title, LocalDate startDate, LocalDate endDate);

	public List<Questionnaire> findByTitleContainingAndStartDateGreaterThanEqualAndEndDateLessThanEqualAndPublishedTrue(String title, LocalDate startDate, LocalDate endDate);

	@Modifying			//insert update delete
	@Transactional		//�n�[
	//�i�H�����gsql�y�k							���W��
	@Query(value = "insert into questionnaire (title, description, is_published, start_date, end_date) "
					// value ��U��@Param���W��										
			+ " values (:title, :description, :isPublished, :startDate, :endDate)", nativeQuery = true)
	public int insert( //
			@Param("title") String title, //
			@Param("description") String description, //
			@Param("isPublished") boolean isPublished, //
			@Param("startDate")LocalDate startDate, //
			@Param("endDate") LocalDate endDate);
	
	
	//²���g�k
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
	
	
	// ���g nativeQuery = true  ���P�� , nativeQuery = false
	// �y�k�����W�٭n�ܦ� entity �� class �W��: ���W�٭n�ܦ��ݩʦW��
	// clearAutomatically = true:�M�����[�ƤW�U��A�Y�M���Ȧs���
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
	//select	���ӧO��� �̦n��nativeQuery = false
	//						�ާ@entity,�ݦ��������غc��k
	@Query(value = "select * from questionnaire "
			+ " where start_date > :startDate", nativeQuery = true)
	public  List<Questionnaire> findByStartDate(@Param("startDate") LocalDate startDate);
	
	//nativeQuery = false					entity���n�������غc��k
	@Query(value = "select new Questionnaire(id, title, description, published, startDate,endDate) from Questionnaire "
			+ " where startDate > :startDate")
	public  List<Questionnaire> findByStartDate1(@Param("startDate") LocalDate startDate);
	
	//nativeQuery = false					���z��Aentity���n�������غc��k
	@Query(value = "select new Questionnaire(id, title,  published) from Questionnaire "
			+ " where startDate >= :startDate")
	public  List<Questionnaire> findByStartDate2(@Param("startDate") LocalDate startDate);
	
	//					�O�W:qu                 as�i�ٲ�  �ĪG�P1
	@Query(value = "select qu from Questionnaire as qu "
	//									�z������or �� and �W�[
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
	
	//order by + limit  limit�u��� nativeQuery = true
	//limit�n��b�y�k���̫�
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

		
		//like  error���� %:title% �� concat�s�� : ('%', :title, '%')
		@Query(value = "select * from questionnaire "
				+ "where title like %:title%", nativeQuery = true)
		public List<Questionnaire> searchTitleLike(@Param("title")String title);
		
		//regexp �u��Φb nativeQuery = true
		@Query(value = "select * from questionnaire "
				+ "where title regexp :title", nativeQuery = true)
		public List<Questionnaire> searchTitleLike2(@Param("title")String title);
		
		
		//regexp or  ���� �ΤU����
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
