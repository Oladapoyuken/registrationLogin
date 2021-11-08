$('.btn-enregistrer').click(function () {
    $('.connexion').addClass('remove-section');
    $('.enregistrer').removeClass('active-section');
    $('.btn-enregistrer').removeClass('active');
    $('.btn-connexion').addClass('active');
});

$('.btn-connexion').click(function () {
    $('.connexion').removeClass('remove-section');
    $('.enregistrer').addClass('active-section');
    $('.btn-enregistrer').addClass('active');
    $('.btn-connexion').removeClass('active');
});

$('#btn-register').click(function(){

    const firstname = $('#firstname').val();
    const lastname = $('#lastname').val();
    const email = $('#email').val();
    const gender = $('#gender').val();
    const phone = $('#phone').val();
    const password = $('#password').val();
    const repeat_password = $('#repeat_password').val();

    if(firstname == '' || lastname == '' || phone == '' || email == '' || password == '' || repeat_password == ''){
        alert("Please fill in all inputs");
    }
    else{
        if(repeat_password != password){
            alert("Passwords does not match");
        }
        else{
            const user = {
                firstName : firstname,
                lastName : lastname,
                email : email,
                gender : gender,
                phoneNumber: phone,
                password : password
            };

            axios.post(
                'http://localhost:8080/api/register',
                user,
                {
                    headers:{
                        "Content-Type": "application/json"
                    }
                }
            ).then((response) => {
                console.log(response);
                alert(response.data.message);
                $('.connexion').removeClass('remove-section');
                $('.enregistrer').addClass('active-section');
                $('.btn-enregistrer').addClass('active');
                $('.btn-connexion').removeClass('active');


            }).catch(err => {
                console.log(err);
            });
        }
    }
});

$('#btn-login').click(function() {
    const username = $('#loginEmail').val();
    const password = $('#loginPassword').val();

    if (username == '' || password == '') {
        alert("Please fill in all inputs");
    }
    else {
        const params = new URLSearchParams();
        params.append("username", username);
        params.append("password", password);

        axios.post(
            'http://localhost:8080/api/login',
            params,
            {
                headers:{
                    "Content-Type": "application/x-www-form-urlencoded"
                }
            }

        ).then((response) => {
            console.log(response);
            if(response.data.status == "success"){

                localStorage.setItem("email", username);
                localStorage.setItem("token", response.data.message);
                window.location.assign("dashboard.html");
            }
            else{
                alert(response.data.message);
            }

        }).catch(err => {
            console.log(err);
        });

    }
});

async function resetPassword(){
    const email = $('#loginEmail').val();
    if(email.trim() === ""){
        alert("Please enter your email address");
    }else{
        if(confirm("This would reset your password, do you want to continue?")){
            try{
                const response = await axios.get(

                    'http://localhost:8080/api/forget?email='+ email,

                    {
                        headers:{
                            'Content-Type': 'application/json'
                        }
                    }

                );
                alert(response.data.message);

            }catch (error){
                console.log(error);
            }

        }
    }

}