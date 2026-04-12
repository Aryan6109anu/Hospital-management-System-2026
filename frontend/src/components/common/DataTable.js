import React from "react";
import "./DataTable.css";

const DataTable = ({ columns, data }) => {
  return (
    <div className="table-container">
      <table className="data-table">

        <thead>
          <tr>
            {columns.map((col, index) => (
              <th key={index}>{col.header}</th>
            ))}
          </tr>
        </thead>

        <tbody>
          {data.length > 0 ? (
            data.map((row, i) => (
              <tr key={i}>
                {columns.map((col, j) => (
                  <td key={j}>
                    {col.render ? col.render(row) : row[col.accessor]}
                  </td>
                ))}
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan={columns.length} className="table-message">
                No Data Found
              </td>
            </tr>
          )}
        </tbody>

      </table>
    </div>
  );
};

export default DataTable;