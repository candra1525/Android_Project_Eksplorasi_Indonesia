<?php
     require('../connection.php');

     $response = array();

     if($_SERVER["REQUEST_METHOD"] == "POST")
     {
          $fullname = $_POST["fullname"];
          $email = $_POST["email"];   
          $role = $_POST["role"];  
          $phone = $_POST["phone"];
          $password = $_POST["password"];
          
          // Mengambil data gambar dari POST
          $foto = $_FILES["foto"]["tmp_name"];
          $foto_name = $_FILES["foto"]["name"];
          
          // Menghindari SQL injection
          $fullname = mysqli_real_escape_string($connection, $fullname);
          $email = mysqli_real_escape_string($connection, $email);
          $role = mysqli_real_escape_string($connection, $role);
          $phone = mysqli_real_escape_string($connection, $phone);
          $password = mysqli_real_escape_string($connection, $password);
          
          $command = "INSERT INTO user (fullname, email, role, phone, foto, password) VALUES ('$fullname', '$email', '$role', '$phone', ?, '$password')";
          $execute = mysqli_query($connection, $command);
          $check = mysqli_affected_rows($connection);

         // Mempersiapkan pernyataan menggunakan prepared statement
    $stmt = mysqli_prepare($connection, $command);
    mysqli_stmt_bind_param($stmt, "b", $foto_data);
    
    // Membaca dan mengunggah file gambar
    $foto_data = file_get_contents($foto);
    mysqli_stmt_send_long_data($stmt, 0, $foto_data);
    
    mysqli_stmt_execute($stmt);

    $check = mysqli_stmt_affected_rows($stmt);

    if ($check > 0) {
        $response["kode"] = 1;
        $response["pesan"] = "Data User berhasil didaftarkan!";
    } else {
        $response["kode"] = 0;
        $response["pesan"] = "Data User gagal didaftarkan!";
    }

    mysqli_stmt_close($stmt);
} else {
    $response["kode"] = 0;
    $response["pesan"] = "Tidak ada data yang berhasil ataupun gagal didaftarkan";
}

echo json_encode($response);

mysqli_close($connection);
?>
