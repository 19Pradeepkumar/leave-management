const cache = {
    myAllLeaves: null,
    myLeavesSummary: null,
    leaveRequests: null,
    leaveViews: null,
    teamMembersData :null
};

document.addEventListener('DOMContentLoaded', function () {

const myLeaves = document.getElementById('my-leaves');
const myTeam = document.getElementById('my-team');

const myLeavesSection = document.getElementById('myLeavesSection');
const myTeamSection = document.getElementById('myTeamSection');
const leaveRequests = document.getElementById('leaveRequests');
const leaveRequestsBody = document.getElementById('leaveRequestsBody');
const myAllLeaves = document.getElementById('myAllLeaves');
const myAllLeavesBody = document.getElementById('myAllLeavesBody');
const leaveViews = document.getElementById('leaveViews');
const leaveViewsBody = document.getElementById('leaveViewsBody');
const profileContainer = document.getElementById('profileContainer');
const teamOptions = document.getElementById('team-options');


//checking whether manager or not

const isManager = localStorage.getItem("isManager");
if (isManager === "true") {
     myTeam.addEventListener('click', showMyTeam);
    document.getElementById('team-leave-requests').style.display = 'inline-block';
    document.getElementById('team-leave-views').style.display = 'inline-block';
}
else{
    myTeam.style.display = 'none'
}


//first loaded page

showMyLeaves();

myLeaves.addEventListener('click', showMyLeaves);

document.getElementById('profile').addEventListener('click', toggleProfileContainer);

function showMyLeaves() {
    myLeaves.style.color = 'white';
    myTeam.style.color = 'grey';
    myLeavesSection.style.display = 'block';
    myTeamSection.style.display = 'none';
    if (cache.myAllLeaves) {
        renderMyAllLeaves(cache.myAllLeaves);
        renderMyLeavesSummary(cache.myLeavesSummary);
    } else {
        myLeavesSummary();
        loadMyAllLeaves();
    }
}

function showMyTeam() {
    myLeaves.style.color = 'grey';
    myTeam.style.color = 'white';
    myLeavesSection.style.display = 'none';
    myTeamSection.style.display = 'block';
    teamOptions.style.display = 'block';
    showManagerLeaveRequests();
    teamMembers();
}


async function loadMyAllLeaves() {
    try {
        const response = await fetch("http://localhost:8092/leave-management/myleaves?action=myLeavesStatus");
        const data = await response.json();
        cache.myAllLeaves = data;
        renderMyAllLeaves(data);
    } catch (error) {
        window.location.href = "/leave-management/login.html"
        console.error('Error fetching my all leaves:', error);
    }
}

function renderMyAllLeaves(data) {
    myAllLeavesBody.innerHTML = '';
    data.forEach(request => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${request.employeeId}</td>
            <td>${request.employeeName}</td>
            <td>${request.fromDate}</td>
            <td>${request.toDate}</td>
            <td>${request.leaveType}</td>
            <td>${request.leaveReason}</td>
            <td>${request.leaveStatus}</td>
        `;
        myAllLeavesBody.appendChild(row);
        setProfile(request.employeeName);
    });
}

async function myLeavesSummary() {
    try {
        const response = await fetch("http://localhost:8092/leave-management/myleaves?action=myLeavesSummary");
        const data = await response.json();
        cache.myLeavesSummary = data;
        renderMyLeavesSummary(data);
    } catch (error) {
        console.error('Error fetching my leaves summary:', error);
    }
}


function setProfile(name) {
    const profileShortName = document.getElementById('profile-appearance');
    const profileEmail = document.getElementById('profile-email');
    const profileName = document.getElementById('profile-name');
    profileName.innerHTML = name;
    //profileEmail.innerHTML = member.employee.email;
    const nameArray = name.split(' ');
    if (nameArray.length > 1) {
        profileShortName.innerHTML = nameArray[0].charAt(0) + nameArray[1].charAt(0);
    } else {
        profileShortName.innerHTML = nameArray[0].charAt(0) + nameArray[0].charAt(1);
    }
}

function toggleProfileContainer() {
    profileContainer.style.display = profileContainer.style.display === 'none' ? 'block' : 'none';
}

window.displayLeaveForm = function () {
    const leaveModal = new bootstrap.Modal(document.getElementById('leaveModal'));
    leaveModal.show();
};



});


//leave application form modal

document.getElementById('leaveApplicationForm').addEventListener('submit', function (event) {
    event.preventDefault();
    submitLeaveForm();
});


function isWeekend(dateString) {
    const date = new Date(dateString);
    const day = date.getDay();
    return day === 0 || day === 6;
}

function isPastDate(dateString) {
    const date = new Date(dateString);
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    return date < today;
}

function validateDates(fromDate, toDate) {
    const fromIsWeekend = isWeekend(fromDate);
    const toIsWeekend = isWeekend(toDate);
    const fromIsPast = isPastDate(fromDate);
    const toIsPast = isPastDate(toDate);

    if (fromIsPast || toIsPast) {
        alert('The date you selected is already passed. Please select a future date.');
        return false;
    }

    if (fromIsWeekend || toIsWeekend) {
        alert('You are selecting a holiday (Saturday or Sunday). Please choose weekdays.');
        return false;
    }

    if (new Date(fromDate) > new Date(toDate)) {
        alert('The "From Date" cannot be later than the "To Date".');
        return false;
    }

    return true;
}



function submitLeaveForm() {
    const fromDate = document.getElementById('fromDate').value;
    const toDate = document.getElementById('toDate').value;
    const leaveType = document.getElementById('leaveType').value;
    const leaveReason = document.getElementById('leaveReason').value;

     if (!validateDates(fromDate, toDate)) {
            return; // Stop the form submission if validation fails
     }

    const data = {
        fromDate: fromDate,
        toDate: toDate,
        leaveType: leaveType,
        leaveReason: leaveReason,
        leaveStatus: 'PENDING'
    };

    fetch('http://localhost:8092/leave-management/myleaves?action=apply_leave', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data)
    })
    .then(response => {
        if (!response.ok) throw new Error('Network response was not ok');
        alert('Leave applied successfully');
        const leaveModal = bootstrap.Modal.getInstance(document.getElementById('leaveModal'));
        if (leaveModal) leaveModal.hide();
        loadMyAllLeaves(); // Refresh the list
    })
    .catch(error => {
        console.error('Error applying leave:', error);
        alert('Failed to apply leave');
    });
}


//second page content

document.getElementById('team-leave-requests').addEventListener('click', showManagerLeaveRequests);
document.getElementById('team-leave-views').addEventListener('click', showManagerLeaveView);

function showManagerLeaveRequests() {
    leaveRequests.style.display = 'block';
    leaveViews.style.display = 'none';
    if (cache.leaveRequests) {
        renderLeaveRequests(cache.leaveRequests);
    } else {
        loadLeaveRequests();
    }
}

function showManagerLeaveView() {
    leaveRequests.style.display = 'none';
    leaveViews.style.display = 'block';
    if (cache.leaveViews) {
        renderLeaveViews(cache.leaveViews);
    } else {
        loadLeaveViews();
    }
}


//leave requests in second page

async function loadLeaveRequests() {
    try {
        const response = await fetch("http://localhost:8092/leave-management/myteamleaves?action=myTeamLeavesRequest");
        const data = await response.json();
        cache.leaveRequests = data;
        renderLeaveRequests(data);
    } catch (error) {
        console.error('Error fetching manager leave requests:', error);
    }
}

function renderLeaveRequests(data) {
   leaveRequestsBody.innerHTML = '';
   data.forEach(request => {
       const row = document.createElement('tr');
       row.id = `row-${request.leaveId}`;
       row.innerHTML = `
           <td>${request.employeeId}</td>
           <td>${request.employeeName}</td>
           <td>${request.fromDate}</td>
           <td>${request.toDate}</td>
           <td>${request.leaveType}</td>
           <td>${request.leaveReason}</td>
           <td>
               <button class="btn btn-success btn-sm" onclick="acceptOrRejectApproval('APPROVED', ${request.leaveId})">Accept</button>
               <button class="btn btn-danger btn-sm" onclick="acceptOrRejectApproval('REJECTED', ${request.leaveId})">Reject</button>
           </td>
       `;
       row.style.cursor = "pointer";
       row.onclick = function() {
                   employeeLeaveDetails(request.employeeId);
               };


       leaveRequestsBody.appendChild(row);
   });
}


function acceptOrRejectApproval(status, leaveId) {
    fetch('http://localhost:8092/leave-management/myteamleaves', {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            leaveId: leaveId,
            status: status
        })
    })
    .then(response => {
        if (!response.ok) throw new Error('Network response was not ok');
        alert('Leave request ' + status);
        const row = document.getElementById(`row-${leaveId}`);
        if (row) {
            row.remove();
        }
        showManagerLeaveRequests();
    })
    .catch(error => {
        console.error('Error updating leave request:', error);
        alert('Failed to update leave request');
    });
  }

//leave views in second page

async function loadLeaveViews() {
    try {
        const response = await fetch("http://localhost:8092/leave-management/myteamleaves?action=myTeamLeavesView");
        const data = await response.json();
        cache.leaveViews = data;
        renderLeaveViews(data);
    } catch (error) {
        console.error('Error fetching manager leave views:', error);
    }
}

function renderLeaveViews(data) {
    leaveViewsBody.innerHTML = '';
    data.forEach(request => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${request.employeeId}</td>
            <td>${request.employeeName}</td>
            <td>${request.fromDate}</td>
            <td>${request.toDate}</td>
            <td>${request.leaveType}</td>
            <td>${request.leaveReason}</td>
            <td>${request.leaveStatus}</td>
        `;
        leaveViewsBody.appendChild(row);
    });
}


//my leaves summary


function renderMyLeavesSummary(cardData) {
const row = document.querySelector('#myleavesSummary .row');
let cardsHtml = '';
cardData.forEach(data => {
    if ((data.gender === 'Male' && data.leaveType === 'MATERNITY_LEAVE') || (data.gender === 'Female' && data.leaveType === 'PATERNITY_LEAVE')) {
        // Skip invalid leave type for gender
    } else {
        cardsHtml += `
            <div class="col-10 col-sm-6 col-md-4 col-lg-3 mb-3">
                <div class="card card-hover-effect">
                    <div class="card-body">
                        <h6 class="card-title">${data.leaveType}</h6>
                        <p class="card-text">Total Leaves: ${data.typeLimit}</p>
                        <p class="card-text">Used Leaves: ${data.used_leaves}</p>
                        <p class="card-text">Remaining Leaves: ${data.remaining_leaves}</p>
                    </div>
                </div>
            </div>
        `;
    }
});

row.innerHTML = cardsHtml;
}




function renderTeamMembers(members) {
const container = document.querySelector('#displayTeamMembers .row');
container.innerHTML = '';

members.forEach(member => {

    console.log(member)
    const cardHtml = `
        <div class="col-md-3 mb-4">
            <div class="card shadow-sm " id="card-${member.employee.employeeId}" onclick="employeeLeaveDetails(${member.employee.employeeId})" style="cursor:pointer;">
                <div class="card-body text-center">
                    <h5 class="card-title">${member.employee.employeeName}</h5>
                    <p class="card-text">ID: ${member.employee.employeeId}</p>
                    <p class="card-text">Email: ${member.employee.employeeEmail}</p>
                </div>
            </div>
        </div>
    `;

    container.innerHTML += cardHtml;
});
}


async function teamMembers() {
if (cache.teamMembersData == null) {
    try {
        const response = await fetch("http://localhost:8092/leave-management/myteamleaves?action=myTeamLeavesSummary");
        const data = await response.json();
        cache.teamMembersData = data;
        renderTeamMembers(cache.teamMembersData);
    } catch (error) {
        console.error('Error fetching my leaves summary:', error);
    }
} else {
    renderTeamMembers(cache.teamMembersData);
}
}




function employeeLeaveDetails(employeeId) {
const member = cache.teamMembersData.find(m => m.employee.employeeId === employeeId);
if (member) {
    const modalId = `employeeModal-${employeeId}`;
    const detailsHtml = `
        <div class="modal fade" id="${modalId}" tabindex="-1" role="dialog" aria-labelledby="employeeModalLabel-${employeeId}" aria-hidden="true">
            <div class="modal-dialog" role="document">
                <div class="modal-content" >
                    <div class="modal-header">
                        <h5 class="modal-title" id="employeeModalLabel-${employeeId}">${member.employee.employeeName}</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <p>Email: ${member.employee.employeeEmail}</p>
                        <p>Date of Birth: ${new Date(member.employee.dateOfBirth).toLocaleDateString()}</p>
                        <h6>Leave Types:</h6>
                        <ul>
                            ${member.leaveTypeDetails.map(leave => `
                                <li>${leave.leaveType}: Used ${leave.used_leaves}, Remaining ${leave.remaining_leaves}, Total ${leave.typeLimit}</li>
                            `).join('')}
                        </ul>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>
    `;

    document.body.insertAdjacentHTML('beforeend', detailsHtml);

    const modalElement = document.getElementById(modalId);
    const modal = new bootstrap.Modal(modalElement);
    modal.show();
    modalElement.addEventListener('hidden.bs.modal', () => {
        modalElement.remove();
    });
} else {
    alert('Employee details not found!');
}
}

