//Get modal element
var modal = document.getElementById('simpleModal');
//Get modal button
var modalBtn = document.getElementById('modalBtn');
//Get close button
var closeBtn = document.getElementsByClassName('closeBtn')[0];


// Listen for open click
modalBtn.addEventListener('click', openModal);
// Listen for close click
closeBtn.addEventListener('click', closeModal);
// Listen for outside click
window.addEventListener('click', clickOutside);

// function to open modal
function openModal(){
    modal.style.display = 'block';
    console.log("ajg pressed open button");

}

// function to open modal
function closeModal(){
    modal.style.display = 'none';
    console.log("ajg pressed closed button");

}

// function to open modal
function clickOutside(e){
    if(e.target == modal) {
       modal.style.display = 'none';
    }
    console.log("ajg pressed click outside");

}
