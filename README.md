# Task_Management2
project
                                  About project

1. Admin bor (yoki MANAGER)

2. User roles: 
	Manager (l - 5), 
	Bussines analyst (l - 4), 
	BE lead(l - 3),
	FE lead(l - 3),
	Backend developer(l - 2),
	Frontend developer(l - 2),
	Scrum Master, 
	QA engineer,
	Tester,
	User(l - (-1));

3. Manager (admin):
	1. Change role of users / remove (if user doesn't have any tasks)
	2. CRUD task
	3. Assign task to a user
	4. See list of users (userni role iga qarab ko'radi)

4. Bussines Analyst:
	1. CRUD task (Update Delete faqat o'zini tasklari)
	2. Assign task to a user (o'zi egasi bo'lgan tasklarni)
	3. See ongoing tasks and assignees

5. BE lead/FE lead:
	1. CRUD task (Update Delete faqat o'zini tasklari) 
	2. Assign task to a user (o'zi egasi bo'lgan tasklarni)
	
	only for backend/fronted

	3. See ongoing tasks and assignees

6. Others (not user):
	Only user operations (do task...)
	1. See my tasks
	2. Change status of my task
	3. My Info

e

7. Task (topshiriq):
	Types: 
		BE_task, 
		FE_task, 
		test, 
		Qa_task, 
		SM_task, 
		BA_task,
	Status:
		Created,
		Assigned,
		In progres,
		Blocked,
		Done;

	name, description

8. Task assign:
	1. task type and user role must match
	2. When showing user list, show whether they have task or not and how many 
