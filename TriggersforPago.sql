CREATE TRIGGER update_insert_total_pagado
AFTER INSERT
   ON pago FOR EACH ROW
UPDATE trabajo t SET t.total_pagado=(select cast(sum(p.valor)as char) from pago p where p.trabajo_id=NEW.trabajo_id) WHERE id=NEW.trabajo_id;

CREATE TRIGGER update_delete_total_pagado
AFTER delete
   ON pago FOR EACH ROW
UPDATE trabajo t SET t.total_pagado=(select cast(sum(p.valor)as char) from pago p where p.trabajo_id=OLD.trabajo_id) WHERE id=OLD.trabajo_id;

CREATE TRIGGER update_update_total_pagado
AFTER update
   ON pago FOR EACH ROW
UPDATE trabajo t SET t.total_pagado=(select cast(sum(p.valor)as char) from pago p where p.trabajo_id=NEW.trabajo_id) WHERE id=NEW.trabajo_id;








-- Dinero recibido por trabajos realizados por tatuador entre dos fechas
select tt.apodo,sum(p.valor) from pago p 
	join trabajo t on p.trabajo_id=t.id 
	join tatuador tt on t.tatuador_id=tt.id 
	where p.fecha between '2018-05-20' and '2018-05-30' 
	group by tt.id;


-- dinero por sedes entre dos fechas
select s.nombre,sum(p.valor) from pago p 
	join trabajo t on p.trabajo_id=t.id 
	join sede s on t.sede_id=s.id 
	where p.fecha between '2018-05-20' and '2018-05-30' 
	group by s.id;
    
-- dinero por rayaton entre dos fechas
select r.fecha,sum(p.valor) from pago p 
	join trabajo t on p.trabajo_id=t.id 
	join rayaton r on t.rayaton_id=r.id 
	where p.fecha between '2018-06-1' and '2018-09-2' 
	group by r.id;
    
 -- trabajos realizados por tatuador  por acabar
select tt.apodo,sum(p.valor) from trabajo p 
	join trabajo t on p.trabajo_id=t.id 
	join tatuador tt on t.tatuador_id=tt.id 
	where p.fecha between '2018-05-20' and '2018-05-30' 
	group by tt.id;









