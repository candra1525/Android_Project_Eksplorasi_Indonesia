<?php
require('../connection.php');

$response = array();

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $id_wisata = $_POST["id_wisata"];
    $nama_wisata = $_POST["nama_wisata"];
    $lokasi_wisata = $_POST["lokasi_wisata"];
    $maps_wisata = $_POST["maps_wisata"];
    $deskripsi_wisata = $_POST["deskripsi_wisata"];
    
    // Menghindari SQL injection
    $nama_wisata = mysqli_real_escape_string($connection, $nama_wisata);
    $lokasi_wisata = mysqli_real_escape_string($connection, $lokasi_wisata);
    $maps_wisata = mysqli_real_escape_string($connection, $maps_wisata);
    $deskripsi_wisata = mysqli_real_escape_string($connection, $deskripsi_wisata);


    // Query untuk memperbarui data wisata berdasarkan ID
    $command = "UPDATE wisata SET nama_wisata='$nama_wisata', lokasi_wisata='$lokasi_wisata', maps_wisata='$maps_wisata', deskripsi_wisata='$deskripsi_wisata' WHERE id_wisata='$id_wisata'";

    // Mengeksekusi perintah SQL
    $result = mysqli_query($connection, $command);

    if ($result) {
        $response["kode"] = 1;
        $response["pesan"] = "Data wisata berhasil diupdate!";
    } else {
        $response["kode"] = 0;
        $response["pesan"] = "Data wisata gagal diupdate!";
    }
} else {
    $response["kode"] = 0;
    $response["pesan"] = "Tidak ada data yang berhasil ataupun gagal diupdate";
}

echo json_encode($response);

mysqli_close($connection);
?>
