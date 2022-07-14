package com.mgs.university.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mgs.university.model.Mahasiswa;
import com.mgs.university.model.Matakuliah;
import com.mgs.university.repository.MahasiswaRepository;
import com.mgs.university.repository.MatakuliahRepository;

@RestController
@RequestMapping("/api")
public class MahasiswaController {

	@Autowired
	MahasiswaRepository mahasiswaRepository;

	@Autowired
	MatakuliahRepository matakuliahRepository;

	@GetMapping("/mahasiswa")
	public ResponseEntity<List<Mahasiswa>> getAllMahasiswa(@RequestParam(required = false) String title) {
		List<Mahasiswa> mahasiswaList = new ArrayList<Mahasiswa>();
		mahasiswaRepository.findAll().forEach(mahasiswaList::add);
		if (mahasiswaList.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(mahasiswaList, HttpStatus.OK);
	}

	@GetMapping("/mahasiswa/{id}")
	public ResponseEntity<Mahasiswa> getMahasiswaById(@PathVariable("id") long id) {
		Mahasiswa mahasiswa = mahasiswaRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Not found Mahasiswa with id = " + id));
		return new ResponseEntity<>(mahasiswa, HttpStatus.OK);
	}

	@PostMapping("/mahasiswa")
	public ResponseEntity<Mahasiswa> createMahasiswa(@RequestBody Mahasiswa mahasiswa) {
		Mahasiswa _mahasiswa = mahasiswaRepository.save(new Mahasiswa(mahasiswa.getNim(), mahasiswa.getNama()));
		return new ResponseEntity<>(_mahasiswa, HttpStatus.CREATED);
	}

	@PutMapping("/mahasiswa/{id}")
	public ResponseEntity<Mahasiswa> updateMahasiswa(@PathVariable("id") long id, @RequestBody Mahasiswa mahasiswa) {
		Mahasiswa _mahasiswa = mahasiswaRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Not found Mahasiswa with id = " + id));
		_mahasiswa.setNim(mahasiswa.getNim());
		_mahasiswa.setNama(mahasiswa.getNama());
		return new ResponseEntity<>(mahasiswaRepository.save(_mahasiswa), HttpStatus.OK);
	}

	@DeleteMapping("/mahasiswa/{id}")
	public ResponseEntity<HttpStatus> deleteMahasiswa(@PathVariable("id") long id) {
		mahasiswaRepository.deleteById(id);

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@DeleteMapping("/mahasiswa")
	public ResponseEntity<HttpStatus> deleteAllMahasiswa() {
		mahasiswaRepository.deleteAll();

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@PutMapping("/mahasiswa/{mahasiswaId}/matakuliah/{matakuliahId}")
	public ResponseEntity<Matakuliah> addMatakuliah(@PathVariable("mahasiswaId") long mahasiswaId,
			@PathVariable("matakuliahId") long matakuliahId) {
		Matakuliah matakuliah = mahasiswaRepository.findById(mahasiswaId).map(mahasiswa -> {
			Matakuliah _matakuliah = matakuliahRepository.findById(matakuliahId)
					.orElseThrow(() -> new ResourceNotFoundException("Not found Matakuliah with id = " + matakuliahId));
			mahasiswa.addMatakuliah(_matakuliah);
			mahasiswaRepository.save(mahasiswa);
			return _matakuliah;
		}).orElseThrow(() -> new ResourceNotFoundException("Not found Mahasiswa with id = " + mahasiswaId));

		return new ResponseEntity<Matakuliah>(matakuliah, HttpStatus.CREATED);
	}

	@DeleteMapping("/mahasiswa/{mahasiswaId}/matakuliah/{matakuliahId}")
	public ResponseEntity<HttpStatus> deleteTagFromTutorial(@PathVariable(value = "mahasiswaId") Long mahasiswaId,
			@PathVariable(value = "matakuliahId") Long matakuliahId) {
		Mahasiswa mahasiswa = mahasiswaRepository.findById(mahasiswaId)
				.orElseThrow(() -> new ResourceNotFoundException("Not found Mahasiswa with id = " + mahasiswaId));

		mahasiswa.removeMatakuliah(matakuliahId);
		mahasiswaRepository.save(mahasiswa);

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@GetMapping("/mahasiswa/{mahasiswaId}/matakuliah")
	public ResponseEntity<Set<Matakuliah>> getMatakuliahList(@PathVariable(value = "mahasiswaId") Long mahasiswaId) {
		Mahasiswa mahasiswa = mahasiswaRepository.findById(mahasiswaId)
				.orElseThrow(() -> new ResourceNotFoundException("Not found Mahasiswa with id = " + mahasiswaId));
		List<Matakuliah> matakuliahList = new ArrayList<>();
		mahasiswa.getMatakuliahs().forEach(matakuliahList::add);
		if (matakuliahList.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(mahasiswa.getMatakuliahs(), HttpStatus.OK);
	}

	@GetMapping("/mahasiswa/count/jurusan/{jurusanId}")
	public ResponseEntity<Integer> getMahasiswaCountJurusan(@PathVariable(value = "jurusanId") Long jurusanId) {
		List<Mahasiswa> mahasiswaList = new ArrayList<>();
		mahasiswaRepository.findAll().forEach(mahasiswaList::add);
		int count = 0;
		for (Mahasiswa mahasiswa : mahasiswaList) {
			if (mahasiswa.getJurusan().getId() == jurusanId) {
				count++;
			}
		}
		return new ResponseEntity<>(count, HttpStatus.OK);
	}

}