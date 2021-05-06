-- ������ռ�
create tablespace itheima
datafile 'itheima.dbf'
size 100m
autoextend on
next 10m;

-- ɾ����ռ�
drop tablespace itheima;

-- �����û�
create user itheima identified by itheima default tablespace itheima;

-- ���û���Ȩ
-- oracle���ݿ��г��ý�ɫ
-- connect�����ӽ�ɫ��������ɫ
-- resource�������߽�ɫ
-- dba����������Ա��ɫ
grant dba to itheima;

-- �л���itheima�û���

-- ����һ��person��
create table person(
       pid number(20),
       pname varchar2(10)
);

-- �޸ı�ṹ
-- ���һ��
alter table person add (gender number(1));
-- �޸�������
alter table person modify gender char(1);
-- �޸�������
alter table person rename column gender to sex;
-- ɾ��һ��
alter table person drop column sex;

-- ��ѯ���м�¼
select * from person;
-- ���һ����¼
insert into person (pid, pname) values (1, 'С��');
commit;
-- �޸�һ����¼
update person set pname='С��' where pid=1;
commit;

-- ����ɾ��
-- ɾ������ȫ����¼
delete from person;
-- ɾ����ṹ
drop table person;
-- ��ɾ�����ٴδ�����Ч����ͬ��ɾ������ȫ����¼
-- ���������������£������ڱ��д�������������£��ò���Ч�ʸ�
-- ����������߲�ѯЧ�ʣ����ǻ�Ӱ����ɾ��Ч��
truncate table person;

-- ���У�Ĭ�ϴ�1��ʼ�����ε�������Ҫ������������ֵʹ��
-- �������κ�һ�ű��������߼��ͱ�����
-- dual�����ֻ��Ϊ�˲�ȫ�﷨��û���κ�����
create sequence s_person;
select s_person.nextval from dual;

-- ���һ����¼
insert into person (pid, pname) values (s_person.nextval, 'С��');
commit;
select * from person;

-- scott�û�������tiger
-- ����scott�û�
alter user scott account unlock;
-- ����scott�û������루Ҳ���������������룩
alter user scott identified by tiger;
-- �л���scott�û���
select * from emp t;
 
-- ���к�����������һ�У�����һ��ֵ

-- �ַ�����
select upper('yes') from dual; -- YES
select lower('YES') from dual; -- yes

-- ��ֵ����
select round(26.18, 1) from dual; -- �������룬����Ĳ�����ʾ������λ��
select trunc(26.18, 1) from dual; -- ֱ�ӽ�ȡ����������λ��������
select mod(10, 3) from dual; -- ������

-- ���ں���
-- ��ѯ��emp��������Ա����ְ�������ڼ���
select sysdate-e.hiredate from emp e;
-- �������˿�
select sysdate+1 from dual;
-- ��ѯ��emp��������Ա����ְ�������ڼ���
select months_between(sysdate, e.hiredate) from emp e;
-- ��ѯ��emp��������Ա����ְ�������ڼ���
select months_between(sysdate, e.hiredate)/12 from emp e;
-- ��ѯ��emp��������Ա����ְ�������ڼ���
select round((sysdate-e.hiredate)/7) from emp e;
-- ת������
-- ����ת�ַ���
select to_char(sysdate, 'fm yyyy-mm-dd hh:mi:ss') from dual; -- fm �Ƿ��һλ����0��ʾΪ��λ��
-- �ַ���ת����
select to_date('2021-4-1 5:26:37', 'fm yyyy-mm-dd hh:mi:ss') from dual;

-- ͨ�ú���
-- ���emp��������Ա������н
-- ��������nullֵ��nullֵ�������������������㣬�������null
select e.sal*12+nvl(e.comm, 0) from emp e; -- nvl �����Ϊnull��ʹ�õ�һ��ֵ��Ϊnull��ʹ�õڶ���ֵ

-- �������ʽ
-- �������ʽ��ͨ��д����mysql��oracleͨ��
-- ��emp����Ա����������
select e.ename, 
       case e.ename
         when 'SMITH' then '����'
           when 'ALLEN' then '�����'
             when 'WARD' then '�����'
               --else '����'
                 end as chinese_name
from emp e;

-- �ж�emp����Ա�����ʣ��������3000��ʾ�����룬����1500����3000��ʾ�е����룬������ʾ������
select e.sal, 
       case
         when e.sal>3000 then '������'
           when e.sal>1500 then '�е�����'
             else '������'
               end as sal_level
from emp e;

-- oracleר���������ʽ
-- oracle�г�������������õ����ţ��������Լ�˫���Ż򲻼�����
select e.ename, 
        decode(e.ename,
          'SMITH', '����',
            'ALLEN', '�����',
              'WARD', '�����',
                '����') ������
from emp e;

-- ���к������ۺϺ������������ڶ��У�����һ��ֵ
select count(1) from emp; -- ��ѯ������
select sum(sal) from emp; -- �����ܺ�
select max(sal) from emp; -- �����
select min(sal) from emp; -- ��͹���
select avg(sal) from emp; -- ƽ������

-- �����ѯ
-- ��ѯÿ�����ŵ�ƽ������
-- �����ѯ�У�������group by�����ԭʼ�У����ܳ�����select����
-- û�г�����group by������У�����select������֣�������ϾۺϺ���
-- �ۺϺ�����һ�����ԣ����԰Ѷ��м�¼���һ��ֵ
select e.deptno, avg(e.sal)--, e.ename
from emp e
group by e.deptno;

-- ��ѯƽ�����ʸ���2000�Ĳ�����Ϣ
-- ��������������ʹ�ñ����ж� 
select e.deptno, avg(e.sal) --asal
from emp e
group by e.deptno
--having asal>2000
having avg(e.sal)>2000;

-- ��ѯÿ�����Ź��ʸ���800��Ա����ƽ�����ʣ�Ȼ���ٲ�ѯ��ƽ�����ʸ���2000�Ĳ���
-- where�ǹ��˷���ǰ�����ݣ�having�ǹ��˷���������
-- ������ʽ��where������group by֮ǰ��having����group by֮��
select e.deptno, avg(e.sal)
from emp e
where e.sal>800
group by e.deptno
having avg(e.sal)>2000;

-- ����ѯ�е�һЩ����
-- �ѿ�����
select *
from emp e, dept d;
-- ��ֵ����
select *
from emp e, dept d
where e.deptno=d.deptno;
-- ������
select *
from emp e inner join dept d
on e.deptno=d.deptno;
-- ��ѯ���в��ż�������Ա������Ϣ�������ӣ�
select *
from emp e right join dept d
on e.deptno=d.deptno;
-- ��ѯ����Ա����Ϣ��Ա����������
select *
from emp e left join dept d
on e.deptno=d.deptno;
-- oracle��ר��������
select *
from emp e, dept d
where e.deptno(+)=d.deptno; -- + ���ı���һ�߾���ʾȫ

-- ��ѯԱ��������Ա���쵼����
-- �����ӣ�վ�ڲ�ͬ�ĽǶȰ�һ�ű��ɶ��ű�
select e1.ename, e2.ename
from emp e1, emp e2
where e1.mgr = e2.empno; -- ��e1����Ա����e2�����쵼��

-- ��ѯԱ��������Ա���������ơ�Ա���쵼������Ա���쵼��������
select e1.ename, d1.dname, e2.ename, d2.dname
from emp e1, emp e2, dept d1, dept d2
where e1.mgr = e2.empno
and e1.deptno = d1.deptno
and e2.deptno = d2.deptno;

-- �Ӳ�ѯ

-- �Ӳ�ѯ����һ��ֵ
-- ��ѯ���ʺ�SCOTTһ����Ա����Ϣ
select * from emp where sal in 
(select sal from emp where ename = 'SCOTT');

-- �Ӳ�ѯ����һ������
-- ��ѯ���ʺ�10�Ų�������Ա��һ����Ա����Ϣ
select * from emp where sal in 
(select sal from emp where deptno = 10);

-- �Ӳ�ѯ����һ�ű�
-- ��ѯÿ��������͹��ʡ���͹���Ա����������Ա�����ڲ�������
-- 1. �Ȳ�ѯÿ��������͹���
select deptno, min(sal) msal
from emp
group by deptno;
-- 2. �������飬�õ����½��
select t.deptno, t.msal, e.ename, d.dname
from (select deptno, min(sal) msal
     from emp
     group by deptno) t, emp e, dept d
where t.deptno = e.deptno
and t.msal = e.sal
and e.deptno = d.deptno;

-- oracle�еķ�ҳ
-- rownum�кţ���������select������ʱ��ÿ��ѯ��һ����¼���ͻ��ڸ����ϼ���һ���кţ��кŴ�1��ʼ�����ε���������������
-- ���������Ӱ��rownum��˳��
-- ����漰�����򣬵���Ҫʹ��rownum�Ļ��������ٴ�Ƕ�ײ�ѯ
select rownum, t.* from (
select rownum, e.* from emp e order by e.sal desc) t;

-- emp���ʵ������к�ÿҳ������¼����ѯ�ڶ�ҳ
-- rownum�кŲ���д�ɴ���һ������
select * from (
select rownum rn, e.* from (
select * from emp order by sal desc
) e where rownum < 11
) where rn > 5;

-- ��ͼ���ṩһ����ѯ�Ĵ��ڣ���������������ԭ��
-- ������ͼ��������dbaȨ�ޣ�

-- ��ѯ��䴴����
create table emp as select * from scott.emp;
select * from emp;
-- ������ͼ��������dbaȨ�ޣ�
create view v_emp as select ename, job from emp;
-- ��ѯ��ͼ
select * from v_emp;
-- �޸���ͼ�����޸�ԭ�����Ƽ���
update v_emp set job = 'CLERK' where ename = 'ALLEN';
commit;
-- ����ֻ����ͼ
create view v_emp1 as select ename, job from emp with read only;
-- ��ͼ������
-- ��һ���������ε�һЩ�����ֶ�
-- �ڶ�����֤�ܲ��ͷֲ����ݼ�ʱͳһ

-- �������ڱ�����Ϲ���һ�����������ﵽ�����߲�ѯЧ�ʵ�Ŀ�ģ����ǻ�Ӱ����ɾ�ĵ�Ч��
-- ��������
-- ������������
create index idx_ename on emp(ename);
-- ���������������������������������е�ԭʼֵ
-- ���к�����ģ����ѯ����Ӱ�������Ĵ���
select * from emp where ename = 'SCOTT';
-- ��������
-- ������������
create index idx_ename_job on emp(ename, job);
-- ����������һ��Ϊ���ȼ�����
-- ���Ҫ��������������������������ȼ������е�ԭʼֵ
select * from emp where ename = 'SCOTT' and job = 'ANALYST'; -- ������������
select * from emp where ename = 'SCOTT' or job = 'ANALYST'; -- ����������
select * from emp where ename = 'SCOTT'; -- ������������

-- pl/sql������ԣ���sql���Ե���չ��ʹsql���Ծ��й��̻���̵�����
-- ��һ��Ĺ��̻�������Ը�������Ч
-- ��Ҫ������д�洢���̡��洢������

-- ��������
-- ��ֵ��������ʹ��:=Ҳ����ʹ��into��ѯ��丳ֵ
declare
  i number(2) := 10;
  s varchar2(10) := 'С��';
  ena emp.ename%type; -- �����ͱ���
  emprow emp%rowtype; -- ��¼�ͱ���
begin
  dbms_output.put_line(i);
  dbms_output.put_line(s);
  select ename into ena from emp where empno = 7788;
  dbms_output.put_line(ena);
  select * into emprow from emp where empno = 7788;
  dbms_output.put_line(emprow.ename || '�Ĺ���Ϊ��' || emprow.job); -- || Ϊ���ӷ�
end;
/

-- pl/sql�е�if�ж�
-- ����С��18�����֣����δ����
-- �������18С��40�����֣����������
-- �������40�����֣����������
declare
  i number(3) := &i;
begin
  if i < 18 then
    dbms_output.put_line('δ����');
  elsif i < 40 then
    dbms_output.put_line('������');
  else
    dbms_output.put_line('������');
  end if;
end;
/

-- pl/sql�е�loopѭ��
-- �����ַ�ʽ���1��10ʮ������
-- whileѭ��
declare
   i number(2) := 1;
begin
  while i < 11 loop
    dbms_output.put_line(i);
    i := i +1;
  end loop;
end;
/

-- exitѭ��
declare
   i number(2) := 1;
begin
  loop
    exit when i > 10;
    dbms_output.put_line(i);
    i := i +1;
  end loop;
end;
/

-- forѭ��
declare

begin
  for i in 1..10 loop
    dbms_output.put_line(i);
  end loop;
end;
/

-- �α꣺���Դ�Ŷ�����󣬶��м�¼
-- ���emp��������Ա��������
declare
  cursor c1 is select * from emp;
  emprow emp%rowtype;
begin
  open c1;
    loop
      fetch c1 into emprow;
      exit when c1%notfound;
      dbms_output.put_line(emprow.ename);
    end loop;
  close c1;
end;
/

-- ��ָ������Ա���ǹ���
declare
  cursor c2(eno emp.deptno%type) is select empno from emp where deptno = eno;
  en emp.empno%type;
begin
  open c2(10);
    loop
      fetch c2 into en;
      exit when c2%notfound;
      update emp set sal=sal+100 where empno=en;
      commit;
    end loop;
  close c2;
end;
/

-- ��ѯ10�Ų���Ա����Ϣ
select * from emp where deptno = 10;

-- �洢���̣���ǰ�Ѿ�����õ�һ��pl/sql���ԣ����������ݿ�˿�ֱ�ӱ�����
-- ��һ��pl/sqlһ�㶼�ǹ̶������ҵ��

-- ��ָ��Ա����100Ԫ
create or replace procedure p1(eno emp.empno%type)
is

begin
  update emp set sal=sal+100 where empno = eno;
  commit;
end;
/

select * from emp where empno = 7788;
-- ����p1
declare

begin
  p1(7788);
end;
/

-- �洢��������洢����������������з���ֵ����������return

-- ͨ���洢����ʵ�ּ���ָ��Ա������н
-- �洢���̺ʹ洢�����Ĳ��������ܴ�����
-- �洢�����ķ���ֵ���Ͳ��ܴ�����
create or replace function f_yearsal(eno emp.empno%type) return number
is
  s number(10);
begin
  select sal*12+nvl(comm, 0) into s from emp where empno = eno;
  return s;
end;
/

-- ����f_yearsal
-- �洢�����ڵ���ʱ������ֵ��Ҫ����
declare
  s number(10);
begin
  s := f_yearsal(7788);
  dbms_output.put_line(s);
end;
/

-- out���Ͳ������ʹ��
-- ʹ�ô洢����������н
create or replace procedure p_yearsal(eno emp.empno%type, yearsal out number)
is
  s number(10);
  c emp.comm%type;
begin
  select sal*12, nvl(comm, 0) into s, c from emp where empno = eno;
  yearsal := s + c;
end;
/

-- ����p_yearsal
declare
  yearsal number(10);
begin
  p_yearsal(7788, yearsal);
  dbms_output.put_line(yearsal);
end;
/

-- in��out���������𣺷����漰��into��ѯ��丳ֵ��:=��ֵ�Ĳ�����������ʹ��out������

-- ��ѯ��Ա��������Ա�����ڲ�������
-- ��scott�û��µ�dept���Ƶ���ǰ�û���
create table dept as select * from scott.dept;
-- ʹ�ô�ͳ��ʽ��ʵ������
select e.ename, d.dname
from emp e, dept d
where e.deptno = d.deptno;
-- ʹ�ô洢������ʵ���ṩһ�����ű�ţ����һ����������
create or replace function f_dna(dno dept.deptno%type) return dept.dname%type
is
  dna dept.dname%type;
begin
  select dname into dna from dept where deptno = dno;
  return dna;
end;
/
-- ʹ��f_dna�洢������ʵ������
select e.ename, f_dna(e.deptno)
from emp e;

-- ���������ƶ�һ����������ɾ�ĵ�ʱ��ֻҪ����ù����Զ��������������
-- ��伶������
-- �м���������������for each row�ľ����м�������
-- ��for each row��Ϊ��ʹ��:old��:new�������һ�м�¼

-- ����һ����¼�����һ����Ա����ְ
-- ��伶������
create or replace trigger t1
after
insert
on person
declare
  
begin
  dbms_output.put_line('һ����Ա����ְ');
end;
/

-- ����t1
insert into person values (1, 'С��');
commit;
select * from person;

-- �м�������
-- ���ܸ�Ա����н
-- raise_application_error(-20001~-20999֮��, '������ʾ')
create or replace trigger t2
before
update
on emp
for each row
declare

begin
  if :old.sal > :new.sal then
    raise_application_error(-20001, '���ܸ�Ա����н');
  end if;
end;
/

-- ����t2
update emp set sal = sal-1 where empno = 7788;
commit;
select * from emp where empno = 7788;

-- ������ʵ�������������м���������
-- ���������û����������֮ǰ���õ�������������ݣ����������е������и�ֵ
create or replace trigger auid
before
insert
on person
for each row
declare
  
begin
  select s_person.nextval into :new.pid from dual;
end;
/

-- ʹ��auidʵ����������
insert into person (pname) values ('С��');
commit;
insert into person values (1, 'С��');
commit;
-- ��ѯperson������
select * from person;







