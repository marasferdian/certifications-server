package com.ibm.certificationsserver;

import com.ibm.certificationsserver.exceptions.ExistentException;
import com.ibm.certificationsserver.model.*;
import com.ibm.certificationsserver.persistence.CertificationRepositoryImp;
import com.ibm.certificationsserver.service.CertificationService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class CertificationsServerApplicationTests {

	@Autowired
	private CertificationService certifService;

	@MockBean
	private CertificationRepositoryImp repo;

	private List<Certification> returnCertifList(){
		List<Certification> lst=new ArrayList<>();
		lst.add(new Certification());
		lst.add(new Certification());
		lst.add(new Certification());

		return lst;
	}

	@Test
	public void getCertificationsTest(){
		when(repo.queryCertifications()).thenReturn(returnCertifList());
		assert certifService.queryCertifications().size()==3:"Test failed";
	}

	@Test
	public void getCertificationTest(){
		Certification certification=new Certification();
		certification.setId(3L);
		certification.setCategory("BEGINNER");
		certification.setCost(22.7);
		certification.setTitle("Test");
		when(repo.queryCertification(3)).thenReturn(certification);
		assert certifService.queryCertification(3L).getTitle().equals("Test");
	}

	@Test
	@WithMockUser(authorities = {"ADMIN"})
	public void testAddCertification() throws ExistentException {
		Certification certification=new Certification();
		certification.setId(3L);
		certification.setCategory("BEGINNER");
		certification.setCost(22.7);
		certification.setTitle("TestAdd");
		when(repo.addCertification(certification)).thenReturn(certification);
		assert certifService.addCertification(certification).getTitle().equals("TestAdd");
	}


	@Test
	public void testAddCustomCertification() throws ExistentException {
		Certification certification=new Certification();
		certification.setId(3L);
		certification.setCategory("BEGINNER");
		certification.setCost(22.7);
		certification.setTitle("TestAddCustom");
		when(repo.addPendingCertification(certification)).thenReturn(certification);
		assert certifService.addPendingCertification(certification).getTitle().equals("TestAddCustom");

	}

	@Test
	public void testUpdateCertification(){
		Certification certification=new Certification();
		certification.setId(3L);
		certification.setCategory("ADVANCED");
		certification.setCost(22.7);
		certification.setTitle("TestUpdate");
		when(repo.updateCertification(certification)).thenReturn(certification);
		assert certifService.updateCertification(certification).getCategory()== Category.ADVANCED;
	}

	@Test
	public void testQueryPending(){
		when(repo.queryCustomCertification()).thenReturn(returnCertifList());
		assert certifService.queryCustomCertification().size()==3;
	}

	@Test
	public void testApproveReject(){
		Certification certification=new Certification();
		certification.setId(3L);
		certification.setCategory("ADVANCED");
		certification.setCost(22.7);
		certification.setTitle("TestReject");
		when(repo.approveOrRejectCustomCertification(certification, Status.REJECTED)).thenReturn(certification);
		assert certifService.approveOrRejectCustomCertification(certification,Status.REJECTED).getTitle().equals("TestReject");
	}

	@Test
	public void testWithFilter(){
		CertificationFilter filter=new CertificationFilter();
		filter.setStatus("PENDING");
		filter.setQuarter("Q2");
		List<RequestDetails> requests=new ArrayList<>();
		requests.add(new RequestDetails());
		requests.add(new RequestDetails());
		when(repo.queryCertificationsWithFilter(filter,3L)).thenReturn(requests);
		assert certifService.queryCertificationsWithFilter(filter,3L).size()==2;
	}

	@Test
	public void testDelete(){
		Certification certification=new Certification();
		certification.setId(3L);
		certification.setCategory("ADVANCED");
		certification.setCost(22.7);
		certification.setTitle("TestUpdate");
		repo.deleteCertification(3L);
		assert certifService.queryCertification(3L)==null;
	}

	@Test
	void contextLoads() {
	}

}
