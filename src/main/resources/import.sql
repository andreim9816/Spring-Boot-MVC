-- departments

insert into department(department_id, name) values (1, 'Cardiologie');
insert into department(department_id, name) values (2, 'Chirurgie');
insert into department(department_id, name) values (3, 'Oftalmologie');

-- address
insert into address(ADDRESS_ID, street, NUMBER, city) values (1, 'Cuza Voda', 10, 'Roman');
insert into address(ADDRESS_ID, street, NUMBER, city) values (2, 'Cuza Voda', 18, 'Roman');
insert into address(ADDRESS_ID, street, NUMBER, city) values (3, 'Bd. Republicii', 1, 'Bucuresti');
insert into address(ADDRESS_ID, street, NUMBER, city) values (4, 'Splaiul Independentei', 204, 'Bucuresti');

-- patients

insert into patient(PATIENT_ID, FIRST_NAME, LAST_NAME, cnp, FK_DEPARTMENT_ID, FK_ADDRESS_ID) values (1,'Andrei', 'Manolache', '19811281729311', 1, 1);
insert into patient(PATIENT_ID, FIRST_NAME, LAST_NAME, cnp, FK_DEPARTMENT_ID, FK_ADDRESS_ID) values (2, 'Ciprian', 'Mocanu', '2991128110899', 1, 2);
insert into patient(PATIENT_ID, FIRST_NAME, LAST_NAME, cnp, FK_DEPARTMENT_ID, FK_ADDRESS_ID) values (3, 'Vlad', 'Duncea', '2981019181709', 2, 3);
insert into patient(PATIENT_ID, FIRST_NAME, LAST_NAME, cnp, FK_DEPARTMENT_ID, FK_ADDRESS_ID) values (4, 'Catalin', 'Oprisan', '6030327260867', 3, 4);

-- doctors

insert into doctor(doctor_id, first_name, last_name, fk_department_id) values (1, 'Victor', 'Firastrau', 1);
insert into doctor(doctor_id, first_name, last_name, fk_department_id) values (2, 'Eugen', 'Mocanu', 1);
insert into doctor(doctor_id, first_name, last_name, fk_department_id) values (3, 'Ana', 'Asmarandei', 1);
insert into doctor(doctor_id, first_name, last_name, fk_department_id) values (4, 'Alina', 'Tura', 2);
insert into doctor(doctor_id, first_name, last_name, fk_department_id) values (5, 'Madalina', 'Dumitriu', 3);
insert into doctor(doctor_id, first_name, last_name, fk_department_id) values (6, 'Andreea', 'Petre', 3);

-- medications

insert into medication(medication_id, name, quantity) values(1, 'Paracetamol', 400);
insert into medication(medication_id, name, quantity) values(2, 'Paracetamol', 800);
insert into medication(medication_id, name, quantity) values(3, 'Paracetamol', 1000);
insert into medication(medication_id, name, quantity) values(4, 'Ibuprofen', 500);
insert into medication(medication_id, name, quantity) values(5, 'Ibuprofen', 1000);
insert into medication(medication_id, name, quantity) values(6, 'Diazepam', 400);
insert into medication(medication_id, name, quantity) values(7, 'Vitamnia C', 1000);
insert into medication(medication_id, name, quantity) values(8, 'Controloc', 400);

-- consults
insert into consult(CONSULT_ID, date, diagnose, symptoms, comment, FK_DOCTOR_ID, FK_PATIENT_ID) values(1,TO_DATE('5-04-2022', 'DD-MM-YYYY'), '-', 'Durere de cap, febra', '-', 1, 1);
insert into consult(CONSULT_ID, date, diagnose, symptoms, comment, FK_DOCTOR_ID, FK_PATIENT_ID) values(2,TO_DATE('1-03-2022', 'DD-MM-YYYY'), '-', 'Greata, ameteala', '-', 2, 2);
insert into consult(CONSULT_ID, date, diagnose, symptoms, comment, FK_DOCTOR_ID, FK_PATIENT_ID) values(3,TO_DATE('12-03-2022', 'DD-MM-YYYY'), '-', 'Insomnii, balonare, ameteala', '-', 2, 2);
insert into consult(CONSULT_ID, date, diagnose, symptoms, comment, FK_DOCTOR_ID, FK_PATIENT_ID) values(4,TO_DATE('20-03-2022', 'DD-MM-YYYY'), 'Traumatism cerebral', 'Nu poate merge drept, ameteli, greturi. Usoare sangerare', '-', 6, 4);


-- prescriptions
insert into prescription(consult_id, medication_id) values(1, 1);
insert into prescription(consult_id, medication_id) values(1, 4);
insert into prescription(consult_id, medication_id) values(1, 7);
insert into prescription(consult_id, medication_id) values(2, 3);
insert into prescription(consult_id, medication_id) values(2, 4);
insert into prescription(consult_id, medication_id) values(2, 6);
insert into prescription(consult_id, medication_id) values(2, 7);
insert into prescription(consult_id, medication_id) values(3, 2);
insert into prescription(consult_id, medication_id) values(3, 8);
insert into prescription(consult_id, medication_id) values(4, 1);
insert into prescription(consult_id, medication_id) values(4, 8);

-- authorities

-- insert into authority(id, role) values(1, 'ADMIN');
-- insert into authority(id, role) values(2, 'DOCTOR');
--
-- users
-- insert into user(id, username, password, enabled, FK_DOCTOR_ID) values(1, 'admin_1', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', 1, null);
-- insert into user(id, username, password, enabled, FK_DOCTOR_ID) values(2, 'doctor_1', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', 1, 1);