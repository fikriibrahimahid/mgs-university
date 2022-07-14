package com.mgs.university.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
public class Mahasiswa {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String nama;

	private String nim;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "fakultas_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Fakultas fakultas;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "jurusan_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Jurusan jurusan;

	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "mahasiswa_matakuliah", joinColumns = {
			@JoinColumn(name = "mahasiswa_id") }, inverseJoinColumns = { @JoinColumn(name = "matakuliah_id") })
	private Set<Matakuliah> matakuliahs = new HashSet<>();

	public Mahasiswa() {

	}

	public Mahasiswa(String nama, String nim) {
		this.nama = nama;
		this.nim = nim;
	}

	public long getId() {
		return id;
	}

	public String getNim() {
		return nim;
	}

	public void setNim(String nim) {
		this.nim = nim;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public Fakultas getFakultas() {
		return fakultas;
	}

	public void setFakultas(Fakultas fakultas) {
		this.fakultas = fakultas;
	}

	public Jurusan getJurusan() {
		return jurusan;
	}

	public void setJurusan(Jurusan jurusan) {
		this.jurusan = jurusan;
	}

	public void addMatakuliah(Matakuliah matakuliah) {
		this.matakuliahs.add(matakuliah);
	}

	public void removeMatakuliah(long matakuliahId) {
		this.matakuliahs.stream().filter(t -> t.getId() == matakuliahId).findFirst().orElse(null);
	}

	public Set<Matakuliah> getMatakuliahs() {
		return matakuliahs;
	}

}
