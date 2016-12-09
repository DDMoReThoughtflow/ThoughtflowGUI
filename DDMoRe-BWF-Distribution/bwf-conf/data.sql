-- Users
INSERT INTO user VALUES (1, now(), now(), 'admin', 'ceb4f32325eda6142bd65215f4c0f371', 'Amos', 'Sanderse', 'AmosSanders@cuvox.de', '+43 1 111111', 'Admin GmbH', 'Vienna', true);
INSERT INTO user VALUES (2, now(), now(), 'manager', 'de835dd5333c20c2b0d5c062aa83fb56', 'Miranda', 'Blind', 'MirandaBlind@einrot.com', '+49 1 111111', 'Manager GmbH', 'Germany', true);
INSERT INTO user VALUES (3, now(), now(), 'scientist', '39aa96b257f8fd64190ccb0ee55f34b2', 'Munoz', 'Robert', 'MunozRobert@einrot.com', '+36 1 111111', 'Admin GmbH', 'Hungary', true);

-- Authorities
INSERT INTO authority VALUES (1, 'ADMIN', 1);
INSERT INTO authority VALUES (2, 'MANAGER', 2);
INSERT INTO authority VALUES (3, 'SCIENTIST', 2);
INSERT INTO authority VALUES (4, 'SCIENTIST', 3);

-- Projects
INSERT INTO project VALUES (1, now(), now(), 'MDLProject', 'https://github.com/halfmanhalfgeek/MDLProject', 'HIGH', 3);

-- Access
INSERT INTO project_access (idUser, idProject) VALUES (1, 1);
INSERT INTO project_access (idUser, idProject) VALUES (2, 1);

-- Reviewers
INSERT INTO project_reviewer (idUser, idProject) VALUES (2, 1);

-- Team for e.g. a manager
INSERT INTO team (idUser, idMember) VALUES (2, 1);
INSERT INTO team (idUser, idMember) VALUES (2, 3);

COMMIT;
