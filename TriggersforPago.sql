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
select tt.apodo,s.nombre,sum(p.valor) from pago p 
	join trabajo t on p.trabajo_id=t.id 
	join tatuador tt on t.tatuador_id=tt.id
    join sede s on tt.sede_id=s.id
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
    
 -- trabajos realizados por tatuador entre dos fechas
 -- and min(c.fecha_y_hora) between '2018-05-1' and '2018-06-20' 
 -- and max(c.fecha_y_hora) between '2018-05-1' and '2018-06-20' 
 -- having min(c.fecha_y_hora) between '2018-05-1' and '2018-06-20'
 
 select e.id, e.apodo, count(e.tf) from (
select t.id,t.apodo,p.id as tf from trabajo p 
	join tatuador t on p.tatuador_id=t.id
    join cita c on c.trabajo_id=p.id
    where p.estado='FINALIZADO'
    group by t.id, p.id
    having min(c.fecha_y_hora) between '2018-05-1' and '2018-06-10'
    and max(c.fecha_y_hora) between '2018-05-1' and '2018-06-10') e
    group by e.id, e.apodo
    
    -- lo mismo pero depurado
;
    
select e.id, e.apodo, count(e.tf) from (
	select t.id,t.apodo,p.id as tf from trabajo p 
		join tatuador t on p.tatuador_id=t.id
		join cita c on c.trabajo_id=p.id
		where p.estado='FINALIZADO' and c.fecha_y_hora between '2018-05-1' and '2018-06-10'
		group by t.id, p.id) e
group by e.id, e.apodo   ;

-- lo mismo pero para sedes

select e.id, e.nombre, count(e.tf) from (
	select s.id,s.nombre,p.id as tf from trabajo p 
		join sede s on p.sede_id=s.id
		join cita c on c.trabajo_id=p.id
		where p.estado='FINALIZADO' and c.fecha_y_hora between '2018-05-1' and '2018-06-10'
		group by s.id, p.id) e
group by e.id, e.nombre   ;

-- lo mismo pero para rayatones 

select e.id, e.fecha, count(e.tf) from (
	select r.id,r.fecha,p.id as tf from trabajo p 
		join rayaton r on p.rayaton_id=r.id
		join cita c on c.trabajo_id=p.id
		where p.estado='FINALIZADO' and c.fecha_y_hora between '2017-05-1' and '2019-06-10'
		group by r.id, p.id) e
group by e.id, e.fecha   






