package test.com.feinno.database;

import static org.junit.Assert.*;
import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;

import com.feinno.database.SqlNormalizer;


public class SqlNormalizerTest extends TestCase{

	public void testSqlNull()
	{
		String fsql = SqlNormalizer.format(null);
		Assert.assertEquals(null, fsql);
	}
	
	public void testSelectWhere()
	{
		//where
		String sql = "select * from table1 where id=1";		
		System.out.println(sql);		
		String fsql = SqlNormalizer.format(sql);		
		System.out.println(fsql);
		Assert.assertEquals("select * from table1 where id=?", fsql);
	}
	
	public void testSelectLike()
	{
	
		//where like in
		String sql = "select * from table1 where id in (1,2,3) and name like '%li%'" +
				" or birthdate between '1988-1-1' and '2010-1-1' ";				
		System.out.println(sql);				
		String fsql = SqlNormalizer.format(sql);		
		System.out.println(fsql);
		String expectedSql =  "select * from table1 where id in (?,?,?) and name like ?" +
				" or birthdate between ? and ? ";
		Assert.assertEquals(expectedSql, fsql);
		
		
	}
	public void testInsertValues()
	{
		String expectedSql = null;
//		//insert
		String sql = "insert into table values(1,'a','123') ";				
		System.out.println(sql);				
		String fsql = SqlNormalizer.format(sql);				
		System.out.println(fsql);
		expectedSql= "insert into table values(?,?,?) ";
		Assert.assertEquals(expectedSql, fsql);
		
		
		
		sql = "insert into table set id = 123-111,name='lichunlei',birthdate=getdate()";				
		System.out.println(sql);				
		fsql = SqlNormalizer.format(sql);				
		System.out.println(fsql);
	}
	public void testInsertSelect()
	{
		String sql = "insert into table from select * from table2 where id>1000";				
		System.out.println(sql);				
		String fsql = SqlNormalizer.format(sql);				
		System.out.println(fsql);
		String expectedSql = "insert into table from select * from table2 where id>?";
		Assert.assertEquals(expectedSql, fsql);
	}
	
	public void testInsertSet()
	{
		String sql = "insert into table set id = id+1,name='lichunlei',birthdate=getdate()";				
		System.out.println(sql);				
		String fsql = SqlNormalizer.format(sql);				
		System.out.println(fsql);
		String expectedSql = "insert into table set id = id+?,name=?,birthdate=getdate()";
		Assert.assertEquals(expectedSql, fsql);
	}
	
	public void testAllSelect()
	{
		String sql = "select d.department_id as d_dept_id, e.department_id as e_dept_id, e.last_name " +
				"from departments d full outer join employees e on d.department_id = e.department_id   order by d.department_id, e.last_name";				
		System.out.println(sql);				
		String fsql = SqlNormalizer.format(sql);				
		System.out.println(fsql);
		
		sql = "select department_id as d_e_dept_id, e.last_name  from departments d full outer join employees e using (department_id) order by department_id, e.last_name " ;
		System.out.println(sql);				
		fsql = SqlNormalizer.format(sql);				
		System.out.println(fsql);
		
		sql = "select d.department_id, e.last_name from m.departments d right outer join n.employees e  on d.department_id = e.department_id  order by d.department_id, e.last_name " ;
		System.out.println(sql);				
		fsql = SqlNormalizer.format(sql);				
		System.out.println(fsql);
		
		
		sql = "select d.department_id, e.last_name from departments d, employees e where d.department_id = e.department_id(+) order by d.department_id, e.last_name" ;
		System.out.println(sql);				
		fsql = SqlNormalizer.format(sql);				
		System.out.println(fsql);
		
		sql = "select times.time_id, product, quantity from inventory  partition by  (product) right outer join times on (times.time_id = inventory.time_id) " +
				" where times.time_id between TO_date('01/04/01', 'DD/MM/YY')   and TO_date('06/04/01', 'DD/MM/YY') order by  2,1" ;
		System.out.println(sql);				
		fsql = SqlNormalizer.format(sql);				
		System.out.println(fsql);
		
		sql = "select * from dual d1  join dual d2 on (d1.dummy = d2.dummy)  join dual d3 on(d1.dummy = d3.dummy)  join dual on(d1.dummy = dual.dummy" ;
		System.out.println(sql);				
		fsql = SqlNormalizer.format(sql);				
		System.out.println(fsql);
		
		sql = "select * from dual t1 join (select * from dual) tt2 using(DUMMY) join (select * from dual) using(DUMMY)" +
				" join (select * from dual) D on(D.dummy=tt3.dummy) inner join (select * from dual) tt2 using(dummy)" +
				" inner join (select * from dual) using(dummy) inner join (select * from dual) D on(D.dummy=t1.dummy)" ;
		System.out.println(sql);				
		fsql = SqlNormalizer.format(sql);				
		System.out.println(fsql);
		
		
		sql = "select * from dual t1, dual t2 join dual t3 using(dummy)" +
				"left outer join dual t4 using(dummy) left outer join dual t5 using(dummy)" ;
		System.out.println(sql);				
		fsql = SqlNormalizer.format(sql);				
		System.out.println(fsql);
		
		sql = "select *  from hdr a  inner join sh s  inner join ca c   on c.id = s.id on a.va = s.va" ;
		System.out.println(sql);				
		fsql = SqlNormalizer.format(sql);				
		System.out.println(fsql);
	
		
		sql = "select d1.*, d2.* from dual d1 cross join dual d2" ;
		System.out.println(sql);				
		fsql = SqlNormalizer.format(sql);				
		System.out.println(fsql);
		
		sql = "select * from v.e where c <> r and  r  not in((select distinct  r  from  v.s ) union" +
				"	(select distinct  r  from v.p )	) and  \"timestamp\" <= 1298505600000" ;
		System.out.println(sql);				
		fsql = SqlNormalizer.format(sql);				
		System.out.println(fsql);
		
		sql = "select interval '4 5:12:10.222' day to second(3),interval '4 5:12' day to minute	,interval '400 5' day(3) to hour" +
				"	,interval '400' day(3),interval '11:12:10.2222222' hour to second(7),interval '11:20' hour to minute,interval '10' hour	," +
				"interval '10:22' minute to second,interval '10' minute,interval '4' day,interval '25' hour,interval '40' minute,interval '120' hour(3)," +
				"interval '30.12345' second(2,4),interval :a day from dual" ;
		System.out.println(sql);				
		fsql = SqlNormalizer.format(sql);				
		System.out.println(fsql);
		
		sql = "select timestamp '2009-10-29 01:30:00',timestamp '2009-10-29 01:30:00' at time zone 'us/pacific'from dual" ;
		System.out.println(sql);				
		fsql = SqlNormalizer.format(sql);				
		System.out.println(fsql);
		
		sql = "select from_tz(cast(to_date('1999-12-01 11:00:00','yyyy-mm-dd hh:mi:ss') as timestamp), 'america/New_York') at time zone 'america/Los_angeles' \"West Coast Time\" from dual" ;
		System.out.println(sql);				
		fsql = SqlNormalizer.format(sql);				
		System.out.println(fsql);
		
		sql = "select *	from  t1, t2 where (trunc(t1.timestamp) between to_date('110226','yymmdd') and to_date('110326','yymmdd'))	and t1.code(+) = 'cn' " +
				"and t1.id(+)=t2.id	and t1.cid=t2.cid	and t1.mid = 1245714070376993504 and t1.tmst >= to_date('110226','yymmdd') column_spec outer_join_sign conditional_operator " +
				"and shipper.alt_party_code(+) is null	and t2.code(+) = 'sh'	and t1.sid(+)=t2.sid and ( ( t1.scode like 'mmm'  and t2.scode like 'xax' )" ;
		System.out.println(sql);				
		fsql = SqlNormalizer.format(sql);				
		System.out.println(fsql);
		
		sql = "select department_id, manager_id  from employees  group by department_id, manager_id  having  department_id = 1" +
				" order by department_id" ;
		System.out.println(sql);				
		fsql = SqlNormalizer.format(sql);				
		System.out.println(fsql);
		
		sql = "lock table a.employees@teste subpartition for (1, 2)   in row share mode   wait 10";
		System.out.println(sql);				
		fsql = SqlNormalizer.format(sql);				
		System.out.println(fsql);
		
		
		
		
	}
	
//	select_statement:
//		<<select d.department_id as d_dept_id, e.department_id as e_dept_id, e.last_name
//		   from departments d full outer join employees e
//		   on d.department_id = e.department_id
//		   order by d.department_id, e.last_name>> OK
//		<<select department_id as d_e_dept_id, e.last_name
//		   from departments d full outer join employees e
//		   using (department_id)
//		   order by department_id, e.last_name>> OK
//		<<select d.department_id, e.last_name
//		   from m.departments d right outer join n.employees e
//		   on d.department_id = e.department_id
//		   order by d.department_id, e.last_name>> OK
//		<<select d.department_id, e.last_name
//		   from departments d, employees e
//		   where d.department_id = e.department_id(+)
//		   order by d.department_id, e.last_name>> OK
	
//		<<select times.time_id, product, quantity from inventory
//		   partition by  (product)
//		   right outer join times on (times.time_id = inventory.time_id)
//		   where times.time_id between TO_date('01/04/01', 'DD/MM/YY')
//		      and TO_date('06/04/01', 'DD/MM/YY')
//		   order by  2,1>> OK
	
//		<<select * from dual d1
//		   join dual d2 on (d1.dummy = d2.dummy)
//		   join dual d3 on(d1.dummy = d3.dummy)
//		   join dual on(d1.dummy = dual.dummy)>> OK
	
//		<<select * from t1
//		   join t2 tt2 using(c)
//		   join t3 tt3 using(d)
//		   join t3 using(d)>> OK
	
//		<<select * from dual t1
//		   join (select * from dual) tt2 using(DUMMY)
//		   join (select * from dual) using(DUMMY)
//		   join (select * from dual) D on(D.dummy=tt3.dummy)
//		   inner join (select * from dual) tt2 using(dummy)
//		   inner join (select * from dual) using(dummy)
//		   inner join (select * from dual) D on(D.dummy=t1.dummy)>> OK
//		<<select * from dual t1
//		   left outer join (select * from dual) tt2 using(DUMMY)
//		   left outer join (select * from dual) using(DUMMY)
//		   left outer join (select * from dual) D on(D.dummy=tt3.dummy)
//		   inner join (select * from dual) tt2 using(dummy)
//		   inner join (select * from dual) using(dummy)
//		   inner join (select * from dual) D on(D.dummy=t1.dummy)>> OK
//		<<select * from dual t1,
//		    (
//		        dual left outer join (select * from dual) tt2 using(DUMMY)
//		    )>> OK
//		<<select * from t1, ( t2 left outer join t3 using(dummy))>> OK
//		<<select * from dual,( dual left outer join tt2 using(DUMMy))>> OK
//		<<select * from t1,
//		       ((((
//		        t2 left outer join t3 using(dummy)
//		       ))))>> OK
//		<<select * from dual t1,
//		(
//		        (
//		                (
//		                        dual t2 join dual t3 using(dummy)
//		                )
//		                        left outer join dual t4 using(dummy)
//		        )
//		                        left outer join dual t5 using(dummy)
//		)
//		>> OK
//		<<select * from dual t1, ( dual t2 join dual t3 using(dummy)) left outer join dual t4 using (dummy)>> OK
//		<<select * from dual t1,
//		                        dual t2 join dual t3 using(dummy)
//		                        left outer join dual t4 using(dummy)
//		                        left outer join dual t5 using(dummy)>> OK
//		<<select *
//		    from hdr a
//		    inner join sh s
//		    inner join ca c
//		    on c.id = s.id
//		    on a.va = s.va>> OK
//		<<select department_id as d_e_dept_id, e.last_name
//		   from departments
//		   full outer join employees on (a=b)
//		   left outer join employees on (a=b)
//		   right outer join employees on (a=b)
//		   join employees on (a=b)
//		   inner join employees on (a=b)
//		   cross join employees
//		   natural join employees
//		>> OK
//		<<select d1.*, d2.* from dual d1 cross join dual d2>> OK
//		<<select d1.*, d2.* from dual cross join dual >> OK
//		<<select * from sys.dual natural join sys.dual >> OK
//		<<select * from v.e
//		where
//			c <> r
//			and  r  not in
//			(
//				(select distinct  r  from  v.s )
//				union
//				(select distinct  r  from v.p )
//			)
//			and  "timestamp" <= 1298505600000>> OK
//		<<select cast(collect(cattr(aname, op, to_char(val), support, confidence)) as cattrs) cl_attrs from t>> OK
//		<<select
//		 interval '4 5:12:10.222' day to second(3)
//		,interval '4 5:12' day to minute
//		,interval '400 5' day(3) to hour
//		,interval '400' day(3)
//		,interval '11:12:10.2222222' hour to second(7)
//		,interval '11:20' hour to minute
//		,interval '10' hour
//		,interval '10:22' minute to second
//		,interval '10' minute
//		,interval '4' day
//		,interval '25' hour
//		,interval '40' minute
//		,interval '120' hour(3)
//		,interval '30.12345' second(2,4)
//		,interval :a day
//		from dual>> OK
//		<<select
//		timestamp '2009-10-29 01:30:00',
//		timestamp '2009-10-29 01:30:00' at time zone 'us/pacific'from dual>> OK
//		<<select from_tz(cast(to_date('1999-12-01 11:00:00','yyyy-mm-dd hh:mi:ss') as timestamp), 'america/New_York') at time zone 'america/Los_angeles' "West Coast Time" from dual>> OK
//		<<select *
//		from "P"
//		where
//		-- note there are no parens around 231092
//		( ( "P"."ID" in 231092 ) )>> OK
//		<< select *
//		from  t1, t2
//		where (trunc(t1.timestamp) between to_date('110226','yymmdd') and to_date('110326','yymmdd'))
//		and t1.code(+) = 'cn'
//		and t1.id(+)=t2.id
//		and t1.cid=t2.cid
//		and t1.mid = 1245714070376993504
//		and t1.tmst >= to_date('110226','yymmdd')
//		-- note: this is possible too "column_spec outer_join_sign conditional_operator
//		and shipper.alt_party_code(+) is null
//		and t2.code(+) = 'sh'
//		and t1.sid(+)=t2.sid
//		and ( ( t1.scode like 'mmm'  and t2.scode like 'xax' ) )>> OK
	
//		<<select distinct X
//		from X,Y,Z
//		where
//		    X.id = Z.id (+)
//		and nvl(X.cid, '^') = nvl(Y.clientid (+), '^')
//		and 0 = Lib.SKU(X.sid, nvl(Z.cid, '^'))>> OK
//		<<select time_id, product
//		   , last_value(quantity ignore nulls) over (partition by product order by time_id) quantity
//		   , last_value(quantity respect nulls) over (partition by product order by time_id) quantity
//		   from ( select times.time_id, product, quantity
//		             from inventory partition by  (product)
//		                right outer join times on (times.time_id = inventory.time_id)
//		   where times.time_id between to_date('01/04/01', 'DD/MM/YY')
//		      and to_date('06/04/01', 'DD/MM/YY'))
//		   order by  2,1>> OK
//		<<select dummy,
//		dummy D,
//		'DDD' as DDD,
//		'DDD' as "DFDF",
//		'1' as
//		from dual>> OK
//		<<select
//		 1
//		 , cursor(select 1 from dual) C1
//		 , cursor(select 2, 3 from dual) as C2
//		 from
//		 table(select 1 from dual)>> OK
//		<<select e1.last_name from employees e1
//		 where f( cursor(select e2.hire_date from employees e2 where e1.employee_id = e2.manager_id), e1.hire_date) = 1
//		order by last_name>> OK
//		<<select title into my_title
//		 from
//		 table(select courses from department where name = 'history')
//		 where name like '%Etruscan%'>> OK
//		<<select deptno
//		        , ename
//		        , hiredate
//		        , listagg(ename, ',') within group (order by hiredate) over (partition by deptno) as employees
//		from emp
//		>> OK
//		<<select trim(both ' ' from '  a  ') from dual where trim(:a) is not null>> OK
//		<<with o as
//		(
//		        select 'a' obj, 'b' link from dual union all
//		        select 'a', 'c' from dual union all
//		        select      'c', 'd' from dual union all
//		        select           'd', 'c' from dual union all
//		        select           'd', 'e' from dual union all
//		        select                'e', 'e' from dual
//		)
//		select
//		  connect_by_root obj root,
//		  level,
//		  obj,link,
//		  sys_connect_by_path(obj||'->'||link,','),
//		  connect_by_iscycle,
//		  connect_by_isleaf
//		from o
//		connect by nocycle obj=prior link
//		start with obj='a'
//		>> OK
//		<<select * from dual where 1 < > 2 and 1 ! = 2 and 1 ^ /*aaa */ = 2>> OK
//		<<select 'A' | | 'B'  from dual>> OK
//		<<select date '1900-01-01' from dual>> OK
//		<<select *
//		from A3
//		where
//			 "A3"."M_ID"="A2"."M_ID"
//			 and "A3"."MI_T" =
//			 any (((
//			        select "A4"."SYS$"."ID" from A4
//			)))
//		>> OK
//		<<select *
//		from A3
//		where
//			 "A3"."M_ID"="A2"."M_ID"
//			 and "A3"."MI_T" =
//			 any ( select "A4"."SYS$"."ID" from A4 )
//		>> OK
//		<<with
//		  REPORTS_TO_101 (EID, EMP_LAST, MGR_ID, REPORTLEVEL) as
//		  (
//		     select EMPLOYEE_ID, LAST_NAME, MANAGER_ID, 0 REPORTLEVEL
//		     from EMPLOYEES
//		     where EMPLOYEE_ID = 101
//		     union all
//		     select E.EMPLOYEE_ID, E.LAST_NAME, E.MANAGER_ID, REPORTLEVEL+1
//		     from REPORTS_TO_101 R, EMPLOYEES E
//		     where R.EID = E.MANAGER_ID
//		  )
//		select EID, EMP_LAST, MGR_ID, REPORTLEVEL
//		from REPORTS_TO_101 R, AUTO A
//		where R.C1 = A.C2
//		order by REPORTLEVEL, EID>> OK
//		<<
//		with
//		  ORG_CHART (EID, EMP_LAST, MGR_ID, REPORTLEVEL, SALARY, JOB_ID) as
//		  (
//		    select EMPLOYEE_ID, LAST_NAME, MANAGER_ID, 0 REPORTLEVEL, SALARY, JOB_ID
//		    from EMPLOYEES
//		    where MANAGER_ID is null
//		  union all
//		    select E.EMPLOYEE_ID, E.LAST_NAME, E.MANAGER_ID,
//		           R.REPORTLEVEL+1 REPORTLEVEL, E.SALARY, E.JOB_ID
//		    from ORG_CHART R, EMPLOYEES E
//		    where R.EID = E.MANAGER_ID
//		  )
//		  search depth first by EMP_LAST set ORDER1
//		select LPAD(' ',2*REPORTLEVEL)||EMP_LAST EMP_NAME, EID, MGR_ID, SALARY, JOB_ID
//		from ORG_CHART
//		order by ORDER1>> OK
//		<<select :1, :X, :1 + 1, 1 + :2 from A where A=:3 and b= : 4 and c= :5and :A = :b>> OK
//		<<select
//		  'Hello'
//		, 'ORACLE.dbs'
//		, 'Jackie''s raincoat'
//		, '09-MAR-98'
//		, ''
//		, ''''
//		, q'!name LIKE '%DBMS_%%'!'
//		, q'<'So,' she said, 'It's finished.'>'
//		, q'{SELECT * FROM employees WHERE last_name = 'Smith';}'
//		, q'"name like '['"'
//		, N'nchar literal'
//		from dual>> OK
//		"select * from tablex, tabley" OK
//		<<with
//		   dept_costs as ( 
//		      select department_name, salary dept_total 
//		         from employees e, departments d 
//		         where e.department_id = d.department_id 
//		      group by department_name), 
//		   avg_cost as ( 
//		      select dept_total avg 
//		      from dept_costs) 
//		select * from dept_costs 
//		   where dept_total = 10>>  OK
//		<<select a.b().c('a').getData(getData(1, 22)).xxx
//		   from employees 
//		   where department_id = 30 
//		   order by last_name>> OK
//		<<select * 
//		   from employees 
//		   where department_id = 30 
//		   order by last_name>> OK
//		<<select last_name, job_id, salary, department_id 
//		   from employees 
//		   where job_id = 'pu_clerk' 
//		   order by last_name>>  OK
//		<<select a.department_id 'department', 
//		   a.num_emp '%_employees', 
//		   a.sal_sum '%_salary' 
//		from
//		(select department_id, num_emp, employees.* 
//		   from employees 
//		   group by department_id) a, 
//		(select total_count 
//		   from employees) b 
//		order by a.department_id>>  OK
//		<<select * from sales partition (sales_q2_2000) s 
//		   where s.amount_sold = 1500 
//		   order by cust_id, time_id, channel_id>>  OK
//		<<select channel_desc, calendar_month_desc, co.country_id, sales
//		   from sales, customers, times, channels, countries co 
//		   where sales.time_id=times.time_id 
//		  group by grouping sets( 
//		      (channel_desc, calendar_month_desc, co.country_id), 
//		      (channel_desc, co.country_id), 
//		      (calendar_month_desc, co.country_id) )>>  OK
//		<<select last_name, employee_id, manager_id from employees 
//		   connect by employee_id = manager_id 
//		   order by last_name >> OK
//		<<select department_id, salary, salary 
//		   from employees 
//		   group by department_id 
//		   having salary = 5000 
//		   order by department_id>> OK
//		<<select department_id, manager_id 
//		   from employees 
//		   group by department_id, manager_id 
//		   having  department_id = 1
//		   order by department_id>> OK
//		<<select * 
//		   from employees 
//		   where job_id = 'pu_clerk' 
//		   order by salary desc>> OK
//		<<select last_name, department_id, salary 
//		   from employees 
//		   order by department_id asc, salary desc, last_name>>  OK
//		<<select last_name, department_id, salary 
//		   from employees 
//		   order by 2 asc, 3 desc, 1>> OK
//		<<select country_name country, 
//		         prod_name prod, 
//		         calendar_year year, 
//		         amount_sold sale, 
//		         amount_sold cnt 
//		    from sales,times,customers,countries,products 
//		    where sales.time_id = times.time_id
//		    group by country_name,prod_name,calendar_year>> OK
//		<<select country,prod,year,s 
//		  from sales_view_ref 
//		  model 
//		    partition by (country)
//		    dimension by (prod, year) 
//		    measures (sale s) 
//		    ignore nav 
//		    unique dimension 
//		    rules upsert sequential order 
//		    (
//		      s[prod='mouse pad'] = 1, 
//		      s['standard mouse'] = 2 
//		    ) 
//		  order by country, prod, year>>  OK
//		<<select country, year, sale, csum 
//		   from 
//		   (select country, year, salex sale 
//		    from sales_view_ref 
//		    group by country, year 
//		   ) 
//		   model dimension by (country, year) 
//		         measures (sale, 0 csum) 
//		         rules 
//		            (
//		              s['standard mouse'] = 2 
//		            )
//		   order by country, year>>  OK
//		<<select e.employee_id, e.salary, e.commission_pct 
//		   from employees e, departments d 
//		   where job_id = 'sa_rep' 
//		   for update 
//		   order by e.employee_id>>  OK
//		<<select e.employee_id, e.salary, e.commission_pct 
//		   from employees e join departments d 
//		   using (department_id) 
//		   where job_id = 'sa_rep'
//		   for update of e.salary 
//		   order by e.employee_id>>  OK
//		<<select * from tablexx 
//		pivot 
//		(xxx(order_total) for order_mode in ('direct' as store, 'online' as internet))>>  OK
//		<<select * from 
//		(select year, order_mode, order_total from orders) 
//		pivot 
//		(yyy(order_total) for order_mode in ('direct' as store, 'online' as internet))>>  OK
//		<<select * from pivot_table 
//		  unpivot (yearly_total for order_mode in (store as 'direct', internet as 'online')) 
//		  order by year, order_mode>>  OK
//		<<select * from pivot_table 
//		  unpivot include nulls 
//		    (yearly_total for order_mode in (store as 'direct', internet as 'online')) 
//		  order by year, order_mode>>  OK
//		<<select last_name, job_id, departments.department_id, department_name 
//		   from employees, departments 
//		   where employees.department_id = departments.department_id 
//		   order by last_name, job_id>>  OK
//		<<select last_name, job_id, departments.department_id, department_name 
//		   from employees, departments 
//		   where employees.department_id = departments.department_id  
//		   order by last_name>> OK
//		<<select last_name, department_id from employees 
//		   where department_id = 
//		     (select department_id from employees 
//		      where last_name = 'lorentz') 
//		   order by last_name, department_id>>  OK
//		<<select e1.last_name||' works for '||e2.last_name 
//		   'employees and their managers'
//		   from employees e1, employees e2 
//		   where e1.manager_id = e2.employee_id  
//		   order by e1.last_name>>  OK
//		<<select d.department_id, e.last_name 
//		   from departments d left outer join employees e 
//		   on d.department_id = e.department_id 
//		   order by d.department_id, e.last_name>>  OK
//		<<select d.department_id, e.last_name 
//		   from departments d right outer join employees e 
//		   on d.department_id = e.department_id 
//		   order by d.department_id, e.last_name>>  OK
//		<<select d.department_id as d_dept_id, e.department_id as e_dept_id, 
//		      e.last_name 
//		   from departments d full outer join employees e
//		     on d.department_id = e.department_id 
//		   order by d.department_id, e.last_name>>  OK
//		<<select department_id as d_e_dept_id, e.last_name 
//		   from departments d full outer join employees e 
//		   using (department_id) 
//		   order by department_id, e.last_name>>  OK
//		<<select times.time_id, product, quantity from inventory 
//		   partition by  (product) 
//		   right outer join times on (times.time_id = inventory.time_id) 
//		   where times.time_id = 1 
//		   order by  2,1>> OK
//		<<select time_id, product, quantity 
//		   from ( select times.time_id, product, quantity 
//		             from inventory partition by  (product) 
//		                right outer join times on (times.time_id = inventory.time_id) 
//		   where times.time_id = 1) 
//		   order by  2,1>> OK
//		<<select t1.department_id, t2.* from hr_info t1, table(t1.people) t2 
//		   where t2.department_id = t1.department_id>>  OK
//		<<select last_name org_chart, employee_id, manager_id, job_id 
//		    from employees 
//		    start with job_id = 'ad_vp' 
//		    connect by prior employee_id = manager_id>> OK
//		<<select last_name, department_name 
//		   from employees@remote, departments 
//		   where employees.department_id = departments.department_id>>  OK
//		"select sysdate from dual" OK
//		"select employees_seq.currval from dual" OK
//
//		table_ref:
//		"tablex" OK
//		<<departments d left outer join employees e 
//		   on d.department_id = e.department_id>>  OK
//		<<departments d right outer join employees e 
//		   on d.department_id = e.department_id>> OK
//		<<departments d full outer join employees e 
//		   using (department_id)>>  OK
//		<<inventory 
//		   partition by  (product) 
//		   right outer join times on (times.time_id = inventory.time_id)>> OK
//
//		table_ref_list:
//		"hr_info t1, table(t1.people) t2 " OK
//		   
//		dml_table_expression_clause:
//		"tablex" OK
//
//		table_ref_aux:
//		"only ( tablex ) " OK
//		"sales partition (sales_q2_2000)" OK
//		"orders sample (10)" OK
//		"orders sample(10) seed (1)" OK
//		"employees as of timestamp systimestamp" OK //TODO fix it
//		<<employees 
//		  versions between timestamp 
//		    systimestamp and systimestamp2>> OK
//		"employees e"  OK
//		"employees as e"  OK
//		<<employees 
//		  versions between timestamp 
//		    systimestamp and systimestamp2 as e>> OK
//		<<employees 
//		  versions between timestamp 
//		    systimestamp and systimestamp2 e>> OK
//
//
//		tableview_name:
//		"tablex" OK
//
//
//		select_statement:
//		"select*from table_1" OK
//		"select * from table_1, table2 where table_1.column1=table_2.column2" OK
//		"select*from table_1 natural inner join table_2" OK
//		"select*from table_1 inner join table_2 on table_1.column1=table_2.column2" OK
//		"select*from table_1 left join table_2 on table_1.column1=table_2.column2" OK
//		"select*from table_1 left outer join table_2 on table_1.column1=table_2.column2" OK
//		"select*from table_1 full outer join table_2 on table_1.column1=table_2.column2" OK
//		"select*from table_1 right join table_2 using (id1,id2)" OK
//
//		"select*from table_1 union select*from table_2" OK
//		"select*from table_1 union all select*from table_2"  OK
//		"select a,b,c from table_1 minus select e,f,g from table_2" OK
//		<<select a,b,c from table_1
//		 union all
//		select e,f,g from table_2
//		 intersect 
//		select*from table_3
//		 union 
//		select*from table_4>>
//		    OK
//		"select a,b into :VarA,:FrmA.FieldB,: _WIN12503 cc from table_1" OK
//
//		"select A from (select B from (select C from d) cross join table_b b)" OK
//		"select 0 from (select B from (select * from C) cross join table_b as b)" OK
//
//		"select * from a where a<> : Var1" OK
//		"select * from b where a<=:frmF1.clsC1.dfDF1.instI1 --EXF4" OK
//		"select*from c where a>aa" OK
//
//		insert_statement:
//		"insert into departments 
//		   values (280, 'recreation', 121, 1700)"  OK
//		<<insert into employees (employee_id, last_name, email, 
//		      hire_date, job_id, salary, commission_pct) 
//		   values (207, 'gregory', 'pgregory@example.com', 
//		      sysdate, 'pu_clerk', 1.2e3, null)>>  OK
//		<<insert into 
//		   (select employee_id.* from employees) 
//		   values (207, 'gregory', 'pgregory@example.com', 
//		      sysdate, 'pu_clerk', 1.2e3, null)>>  OK
//		<<insert into bonuses 
//		   select employee_id.* from employees>>  OK
//		<<insert into raises 
//		   select salary.* from employees 
//		   where commission_pct > .2 
//		   log errors into errlog ('my_bad') reject limit 10>>  OK
//		<<insert into employees@remote 
//		   values (8002, 'juan', 'fernandez', 'juanf@hr.example.com', null, 
//		   to_date('04-oct-1992', 'dd-mon-yyyy'), 'sh_clerk', 3000, 
//		   null, 121, 20)>>  OK
//		<<insert into departments 
//		   values  (departments_seq.nextval, 'entertainment', 162, 1400)>>  OK
//		<<insert into employees 
//		      (employee_id, last_name, email, hire_date, job_id, salary)
//		   values 
//		   (employees_seq, 'doe', 'john.doe@example.com', 
//		       sysdate, 'sh_clerk', 2400) 
//		   returning salary, job_id into :bnd1, :bnd2>>  OK
//		<<insert into books values ( 
//		   'an autobiography', person_t('bob', 1234))>>  OK
//		<<insert into lob_tab 
//		   select salary.* from employees>>  OK
//		<<insert all 
//		      into sales (prod_id, cust_id, time_id, amount) 
//		      values (product_id, customer_id, weekly_start_date, sales_sun) 
//		      into sales (prod_id, cust_id, time_id, amount) 
//		      values (product_id, customer_id, weekly_start_date, sales_mon) 
//		      into sales (prod_id, cust_id, time_id, amount) 
//		      values (product_id, customer_id, weekly_start_date, sales_tue) 
//		      into sales (prod_id, cust_id, time_id, amount) 
//		      values (product_id, customer_id, weekly_start_date, sales_wed) 
//		      into sales (prod_id, cust_id, time_id, amount) 
//		      values (product_id, customer_id, weekly_start_date, sales_thu)
//		     into sales (prod_id, cust_id, time_id, amount) 
//		      values (product_id, customer_id, weekly_start_date, sales_fri) 
//		      into sales (prod_id, cust_id, time_id, amount) 
//		      values (product_id, customer_id, weekly_start_date, sales_sat) 
//		   select sales_input_table.* 
//		      from sales_input_table>>  OK
//		<<insert all 
//		   when order_total < 1000000 then 
//		      into small_orders 
//		   when order_total < 2000000 then 
//		      into medium_orders 
//		   when order_total > 2000000 then 
//		      into large_orders 
//		   select orders.* from orders>>  OK
//		<<insert all 
//		   when order_total < 100000 then 
//		      into small_orders 
//		   when order_total < 200000 then 
//		      into medium_orders 
//		   else 
//		      into large_orders 
//		   select orders.* from orders>>  OK
//		<<insert all 
//		   when ottl < 100000 then 
//		      into small_orders 
//		         values(oid, ottl, sid, cid) 
//		   when ottl > 100000 and ottl < 200000 then 
//		      into medium_orders 
//		         values(oid, ottl, sid, cid) 
//		   when ottl > 200000 then 
//		      into large_orders 
//		         values(oid, ottl, sid, cid) 
//		   when ottl > 290000 then 
//		      into special_orders 
//		   select o.* from orders o>>  OK
//		<<insert all 
//		   when ottl < 100000 then 
//		      into small_orders 
//		         values(oid, ottl, sid, cid) 
//		   when ottl > 100000 and ottl < 200000 then 
//		      into medium_orders 
//		         values(oid, ottl, sid, cid) 
//		   when ottl > 200000 then 
//		      into large_orders 
//		         values(oid, ottl, sid, cid) 
//		   when ottl > 290000 then 
//		      into special_orders 
//		   when ottl > 200000 then 
//		      into large_orders 
//		         values(oid, ottl, sid, cid) 
//		   select o.* from orders o>>  OK
//
//		delete_statement:
//		<<delete from product_descriptions 
//		   where language_id = 'ar'>>  OK
//		<<delete from employees 
//		   where job_id = 'sa_rep' 
//		   and commission_pct < .2>>  OK
//		<<delete from (select employees.* from employees) 
//		   where job_id = 'sa_rep' 
//		   and commission_pct < .2>>  OK
//		<<delete from hr.locations@remote 
//		   where location_id > 3000>>  OK
//		<<delete from sales partition (sales_q1_1998) 
//		   where amount_sold > 1000>>  OK
//		<<delete from employees
//		   where job_id = 'sa_rep'
//		   and hire_date < 10
//		   returning salary into :bnd1>>  OK
//
//		update_statement:
//		<<update employees 
//		   set commission_pct = null 
//		   where job_id = 'sh_clerk'>>  OK
//		<<update employees set 
//		    job_id = 'sa_man', salary = 1000, department_id = 120 
//		    where first_name = 'douglas grant'>>  OK
//		<<update employees@remote
//		   set salary = 1.1
//		   where last_name = 'baer'>>  OK
//		<<update employees a 
//		    set department_id = 
//		        (select departments.*
//		            from departments 
//		            where location_id = '2100'), 
//		        (salary, commission_pct) = 
//		        (select departments.*
//		            from departments 
//		            where location_id = '2100') 
//		    where department_id > 1>>  OK
//		<<update sales partition (sales_q1_1999) s 
//		   set s.promo_id = 494 
//		   where amount_sold > 1000>>  OK
//		<<update people_demo1 p set value(p) = 
//		   (select departments.* from departments) 
//		   where p.department_id = 10>>  OK
//		<<update employees 
//		  set job_id ='sa_man', salary = 1000, department_id = 140 
//		  where last_name = 'jones'
//		  returning salary, last_name, department_id into :bnd1, :bnd2, :bnd3>>  OK
//		<<update employees 
//		   set salary = 1.1 
//		   where department_id = 100 
//		   returning salary into :bnd1>>  OK
//
//		merge_statement:
//		<<merge into bonuses d 
//		   using (select employee_id.* from employees) s 
//		   on (employee_id = a) 
//		   when matched then update set d.bonus = bonus 
//		     delete where (salary > 8000)
//		   when not matched then insert (d.employee_id, d.bonus) 
//		     values (s.employee_id, s.salary)
//		     where (s.salary <= 8000)>> OK
//		<<merge into bonuses d 
//		   using (select employee_id.* from employees) s 
//		   on (employee_id = a) 
//		   when matched then update set d.bonus = bonus 
//		     delete where (salary > 8000)
//		   when not matched then insert (d.employee_id, d.bonus) 
//		     values (s.employee_id, s.salary)
//		     where (s.salary <= 8000)
//		   log errors into errlog ('my_bad') reject limit 10>> OK
//
//		lock_table_statement:
//		<<lock table employees 
//		   in exclusive mode 
//		   nowait>>  OK
//		<<lock table employees 
//		   in exclusive mode 
//		   wait 10>>  OK
//		<<lock table employees 
//		   in share update mode 
//		   wait 10>>  OK
//		<<lock table employees 
//		   in share mode 
//		   wait 10>>  OK
//		<<lock table employees 
//		   in row share mode 
//		   wait 10>>  OK
//		<<lock table a.employees 
//		   in row share mode 
//		   wait 10>>  OK
//		<<lock table a.employees@teste partition for (1, 2)
//		   in row share mode 
//		   wait 10>> OK
//		<<lock table a.employees@teste partition (1)
//		   in row share mode 
//		   wait 10>> OK
//		<<lock table a.employees@teste subpartition for (1, 2)
//		   in row share mode 
//		   wait 10>> OK
//		<<lock table a.employees@teste subpartition (1)
//		   in row share mode 
//		   wait 10>> OK
}
