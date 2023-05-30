package interfaceAplication;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class ProgramInterface {
    
    private DepartmentDao departmentDao;
    
    private JFrame frame;
    private JTextField idTextField;
    private JTextField nameTextField;
    
    public ProgramInterface() {
        departmentDao = DaoFactory.createDepartmentDao();
        initialize();
    }
    
    private void initialize() {
        frame = new JFrame("Department CRUD");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);
        
        JLabel idLabel = new JLabel("ID:");
        idTextField = new JTextField(10);
        
        JLabel nameLabel = new JLabel("Name:");
        nameTextField = new JTextField(20);
        
        JButton findByIdButton = new JButton("Find by ID");
        findByIdButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int id = Integer.parseInt(idTextField.getText());
                Department department = departmentDao.findById(id);
                if (department != null) {
                    nameTextField.setText(department.getName());
                } else {
                    JOptionPane.showMessageDialog(frame, "Department not found");
                }
            }
        });
        
        JButton findAllButton = new JButton("Find All");
        findAllButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                List<Department> departments = departmentDao.findAll();
                if (departments.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "No departments found");
                } else {
                    StringBuilder sb = new StringBuilder();
                    for (Department department : departments) {
                        sb.append(department).append("\n");
                    }
                    JOptionPane.showMessageDialog(frame, sb.toString());
                }
            }
        });
        
        JButton insertButton = new JButton("Insert");
        insertButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = nameTextField.getText();
                Department newDepartment = new Department(null, name);
                departmentDao.insert(newDepartment);
                JOptionPane.showMessageDialog(frame, "Department inserted! New ID: " + newDepartment.getId());
            }
        });
        
        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int id = Integer.parseInt(idTextField.getText());
                String name = nameTextField.getText();
                Department department = departmentDao.findById(id);
                if (department != null) {
                    department.setName(name);
                    departmentDao.update(department);
                    JOptionPane.showMessageDialog(frame, "Update completed");
                } else {
                    JOptionPane.showMessageDialog(frame, "Department not found");
                }
            }
        });
        
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int id = Integer.parseInt(idTextField.getText());
                Department department = departmentDao.findById(id);
                if (department != null) {
                    departmentDao.deleteById(id);
                    JOptionPane.showMessageDialog(frame, "Delete completed");
                } else {
                    JOptionPane.showMessageDialog(frame, "Department not found");
                }
            }
        });
        
        frame.setLayout(new FlowLayout());
        frame.add(idLabel);
        frame.add(idTextField);
        frame.add(nameLabel);
        frame.add(nameTextField);
        frame.add(findByIdButton);
        frame.add(findAllButton);
        frame.add(insertButton);
        frame.add(updateButton);
        frame.add(deleteButton);
        
        frame.setVisible(true);
    }
    
    public static void main(String[] args) {
        new ProgramInterface();
    }
}

