package me.loule.hipopothalous.model;

public class Employee {
    private String name;
    private String poste;
    private Double hoursWorked;
    private Double age;
    private Double salary;

    public Employee(String name, String poste, Double hoursWorked, Double age, Double salary) {
        this.name = name;
        this.poste = poste;
        this.hoursWorked = hoursWorked;
        this.age = age;
        this.salary = salary;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPoste() {
        return poste;
    }

    public void setPoste(String poste) {
        this.poste = poste;
    }

    public Double getHoursWorked() {
        return hoursWorked;
    }

    public void setHoursWorked(Double hoursWorked) {
        this.hoursWorked = hoursWorked;
    }

    public Double getAge() {
        return age;
    }

    public void setAge(Double age) {
        this.age = age;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                ", poste='" + poste + '\'' +
                ", hoursWorked=" + hoursWorked +
                ", age=" + age +
                ", salary=" + salary +
                '}';
    }
}
