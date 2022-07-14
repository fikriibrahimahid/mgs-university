package com.mgs.university.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Matakuliah {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String kode;

	private String nama;

	public Matakuliah() {

	}

	public Matakuliah(String kode, String nama) {
		this.kode = kode;
		this.nama = nama;
	}

	public long getId() {
		return id;
	}

	public String getKode() {
		return kode;
	}

	public void setKode(String kode) {
		this.kode = kode;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

}