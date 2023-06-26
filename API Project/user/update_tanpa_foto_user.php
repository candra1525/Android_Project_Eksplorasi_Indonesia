<?php
require('../connection.php');

$response = array();

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $id_user = $_POST["id_user"];
    $fullname = $_POST["fullname"];
    $email = $_POST["email"];
    $role = $_POST["role"];
    $phone = $_POST["phone"];
    $password = $_POST["password"];

    // Menghindari SQL injection
    $fullname = mysqli_real_escape_string($connection, $fullname);
    $email = mysqli_real_escape_string($connection, $email);
    $role = mysqli_real_escape_string($connection, $role);
    $phone = mysqli_real_escape_string($connection, $phone);
    $password = mysqli_real_escape_string($connection, $password);

    $command = "UPDATE user SET fullname = '$fullname', email = '$email', role = '$role', phone = '$phone', password = '$password' WHERE id_user = '$id_user'";

    // Mengeksekusi perintah SQL
    $result = mysqli_query($connection, $command);

    if ($result) {
        $response["kode"] = 1;
        $response["pesan"] = "Data User berhasil diupdate!";
    } else {
        $response["kode"] = 0;
        $response["pesan"] = "Data User gagal diupdate!";
    }
} else {
    $response["kode"] = 0;
    $response["pesan"] = "Tidak ada data yang berhasil ataupun gagal diupdate";
}

echo json_encode($response);

mysqli_close($connection);
?>
