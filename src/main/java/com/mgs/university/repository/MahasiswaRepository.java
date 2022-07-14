package com.mgs.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mgs.university.model.Mahasiswa;

public interface MahasiswaRepository extends JpaRepository<Mahasiswa, Long> {

}