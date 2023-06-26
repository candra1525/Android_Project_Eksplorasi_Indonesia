<?php
require('../connection.php');

$response = array();

// Query untuk mengambil data kuliner dari tabel
$command = "SELECT * FROM kuliner";

// Mengeksekusi perintah select
$result = mysqli_query($connection, $command);

// Mengecek jumlah baris hasil query
$rows = mysqli_num_rows($result);

if ($rows > 0) {
    $response["kode"] = 1;
    $response["pesan"] = "Data kuliner tersedia";
    $response["dataKuliner"] = array();

    // Menarik data dari database
    while ($row = mysqli_fetch_assoc($result)) {
        $temp["id_kuliner"] = $row["id_kuliner"];
        $temp["nama_kuliner"] = $row["nama_kuliner"];
        $temp["asal_kuliner"] = $row["asal_kuliner"];
        $temp["deskripsi_kuliner"] = $row["deskripsi_kuliner"];
        
        // Mengambil data gambar dari database
        $foto_kuliner = $row["foto_kuliner"];
        
        // Mengubah data gambar menjadi base64
        $base64_image = base64_encode($foto_kuliner);
        
        // Menggabungkan data gambar dengan data lainnya
        $temp["foto_kuliner"] = $base64_image;
        
        array_push($response["dataKuliner"], $temp);
    }
} else {
    $response["kode"] = 0;
    $response["pesan"] = "Data kuliner tidak tersedia";
}

// Mengubah menjadi format json
echo json_encode($response);

// Menutup koneksi
mysqli_close($connection);
?>
