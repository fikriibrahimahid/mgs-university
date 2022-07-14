package com.mgs.university.controller;

import java.util.ArrayList;
import java.util.List;

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
public class MatakuliahController {

	@Autowired
	MatakuliahRepository matakuliahRepository;

	@Autowired
	MahasiswaRepository mahasiswaRepository;

	@GetMapping("/matakuliah")
	public ResponseEntity<List<Matakuliah>> getAllMatakuliah(@RequestParam(required = false) String title) {
		List<Matakuliah> matakuliahList = new ArrayList<Matakuliah>();
		matakuliahRepository.findAll().forEach(matakuliahList::add);
		if (matakuliahList.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(matakuliahList, HttpStatus.OK);
	}

	@GetMapping("/matakuliah/{id}")
	public ResponseEntity<Matakuliah> getMatakuliahById(@PathVariable("id") long id) {
		Matakuliah matakuliah = matakuliahRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Not found Matakuliah with id = " + id));
		return new ResponseEntity<>(matakuliah, HttpStatus.OK);
	}

	@PostMapping("/matakuliah")
	public ResponseEntity<Matakuliah> createMatakuliah(@RequestBody Matakuliah matakuliah) {
		Matakuliah _matakuliah = matakuliahRepository.save(new Matakuliah(matakuliah.getKode(), matakuliah.getNama()));
		return new ResponseEntity<>(_matakuliah, HttpStatus.CREATED);
	}

	@PutMapping("/matakuliah/{id}")
	public ResponseEntity<Matakuliah> updateMatakuliah(@PathVariable("id") long id,
			@RequestBody Matakuliah matakuliah) {
		Matakuliah _matakuliah = matakuliahRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Not found Matakuliah with id = " + id));
		_matakuliah.setKode(matakuliah.getKode());
		_matakuliah.setNama(matakuliah.getNama());

		return new ResponseEntity<>(matakuliahRepository.save(_matakuliah), HttpStatus.OK);
	}

	@DeleteMapping("/matakuliah/{id}")
	public ResponseEntity<HttpStatus> deleteMatakuliah(@PathVariable("id") long id) {
		matakuliahRepository.deleteById(id);

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@DeleteMapping("/matakuliah")
	public ResponseEntity<HttpStatus> deleteAllMatakuliah() {
		matakuliahRepository.deleteAll();

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@GetMapping("/matakuliah/{matakuliahId}/mahasiswa/count")
	public ResponseEntity<Integer> getMatakuliahMahasiswaCount(
			@PathVariable(value = "matakuliahId") Long matakuliahId) {
		matakuliahRepository.findById(matakuliahId)
				.orElseThrow(() -> new ResourceNotFoundException("Not found Matakuliah with id = " + matakuliahId));
		List<Mahasiswa> mahasiswaList = new ArrayList<>();
		mahasiswaRepository.findAll().forEach(mahasiswaList::add);
		int count = 0;
		for (Mahasiswa mahasiswa : mahasiswaList) {
			List<Matakuliah> matakuliahList = new ArrayList<>();
			mahasiswa.getMatakuliahs().forEach(matakuliahList::add);
			for (Matakuliah matakuliah : matakuliahList) {
				if (matakuliah.getId() == matakuliahId) {
					count++;
					break;
				}
			}
		}
		return new ResponseEntity<>(count, HttpStatus.OK);
	}

}