<?php
require('../connection.php');

$response = array();

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $nama_wisata = $_POST["nama_wisata"];
    $lokasi_wisata = $_POST["lokasi_wisata"];
    $maps_wisata = $_POST["maps_wisata"];
    $deskripsi_wisata = $_POST["deskripsi_wisata"];

    // Menghindari SQL injection
    $nama_wisata = mysqli_real_escape_string($connection, $nama_wisata);
    $lokasi_wisata = mysqli_real_escape_string($connection, $lokasi_wisata);
    $maps_wisata = mysqli_real_escape_string($connection, $maps_wisata);
    $deskripsi_wisata = mysqli_real_escape_string($connection, $deskripsi_wisata);

    // Mengambil data gambar dari POST
    $foto_wisata = $_FILES["foto_wisata"]["tmp_name"];
    $foto_wisata_name = $_FILES["foto_wisata"]["name"];

    // Membaca dan mengunggah file gambar
    $foto_wisata_data = file_get_contents($foto_wisata);

    // Mempersiapkan pernyataan menggunakan prepared statement
    $stmt = mysqli_prepare($connection, "INSERT INTO wisata (nama_wisata, lokasi_wisata, maps_wisata, foto_wisata, deskripsi_wisata) VALUES (?, ?, ?, ?, ?)");

    // Mengikat parameter dengan tipe data yang sesuai
    mysqli_stmt_bind_param($stmt, "sssss", $nama_wisata, $lokasi_wisata, $maps_wisata, $foto_wisata_data, $deskripsi_wisata);

    // Eksekusi pernyataan
    if (mysqli_stmt_execute($stmt)) {
        $response["kode"] = 1;
        $response["pesan"] = "Data wisata berhasil didaftarkan!";
    } else {
        $response["kode"] = 0;
        $response["pesan"] = "Data wisata gagal didaftarkan!";
    }

    mysqli_stmt_close($stmt);
} else {
    $response["kode"] = 0;
    $response["pesan"] = "Tidak ada data yang berhasil ataupun gagal didaftarkan";
}

echo json_encode($response);

mysqli_close($connection);
?>
