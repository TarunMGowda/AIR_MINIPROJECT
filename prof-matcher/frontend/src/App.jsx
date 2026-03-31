import { useState } from 'react'
import axios from 'axios'
import './App.css'

function App() {
  const [query, setQuery] = useState('')
  const [campus, setCampus] = useState('All')
  const [department, setDepartment] = useState('All')
  const [professors, setProfessors] = useState([])
  const [loading, setLoading] = useState(false)
  const [hasSearched, setHasSearched] = useState(false)
  
  // NEW STATE: Keeps track of which professor card was clicked
  const [selectedProf, setSelectedProf] = useState(null)

  const handleSearch = async (e) => {
    e.preventDefault();
    if (!query.trim()) return;

    setLoading(true);
    setHasSearched(true);
    setSelectedProf(null); // Reset detail view on new search
    
    try {
      const url = `http://localhost:8080/api/search?q=${query}&campus=${campus}&dept=${department}&page=1&size=15`;
      const response = await axios.get(url);
      setProfessors(response.data);
    } catch (error) {
      console.error("Error fetching data:", error);
      alert("Failed to connect to the backend.");
    } finally {
      setLoading(false);
    }
  }

  // --- VIEW 1: THE DETAILED PROFILE PAGE ---
  if (selectedProf) {
    return (
      <div className="app-container">
        <button className="back-button" onClick={() => setSelectedProf(null)}>
          &larr; Back to Search Results
        </button>
        
        <div className="profile-detail-card">
          <div className="profile-header-large">
            {selectedProf.image && selectedProf.image.length > 5 ? (
              <img src={selectedProf.image} alt={selectedProf.name} className="prof-image-large" />
            ) : (
              <div className="prof-image-placeholder-large">{selectedProf.name.charAt(0)}</div>
            )}
            <div className="profile-titles">
              <h1>{selectedProf.name}</h1>
              <h2>{selectedProf.designation}</h2>
              <div className="tag-container">
                <span className="highlight-tag">{selectedProf.campus || 'Campus N/A'}</span>
                <span className="highlight-tag">{selectedProf.department || 'Dept N/A'}</span>
              </div>
            </div>
          </div>

          <div className="profile-body">
            <div className="contact-info">
              <h3>Contact Information</h3>
              <p><strong>Email:</strong> <a href={`mailto:${selectedProf.mail}`}>{selectedProf.mail}</a></p>
              {selectedProf.phone && <p><strong>Phone:</strong> {selectedProf.phone}</p>}
            </div>

            {selectedProf.about && selectedProf.about !== "Not listed" && (
              <div className="info-section">
                <h3>About</h3>
                <p>{selectedProf.about}</p>
              </div>
            )}

            {selectedProf.education && (
              <div className="info-section">
                <h3>Education Background</h3>
                <p className="formatted-text">{selectedProf.education}</p>
              </div>
            )}

            {selectedProf.research && (
              <div className="info-section">
                <h3>Research Areas</h3>
                <p className="formatted-text">{selectedProf.research}</p>
              </div>
            )}

            {selectedProf.teaching && (
              <div className="info-section">
                <h3>Subjects Taught</h3>
                <p className="formatted-text">{selectedProf.teaching}</p>
              </div>
            )}

            {selectedProf.pubJournals && selectedProf.pubJournals.trim() !== "" && (
              <div className="info-section">
                <h3>Publications</h3>
                <p className="formatted-text">{selectedProf.pubJournals}</p>
              </div>
            )}

            {selectedProf.responsibilities && (
              <div className="info-section">
                <h3>University Responsibilities</h3>
                <p className="formatted-text">{selectedProf.responsibilities}</p>
              </div>
            )}
          </div>
        </div>
      </div>
    );
  }

  // --- VIEW 2: THE MAIN SEARCH GRID (Original View) ---
  return (
    <div className="app-container">
      <header className="hero-section">
        <h1>PESU Scholar Match</h1>
        <p>Find your ideal capstone mentor based on research alignment.</p>
        
        <form onSubmit={handleSearch} className="search-container">
          <div className="search-bar">
            <input 
              type="text" 
              placeholder="e.g., 'Cybersecurity in IoT' or 'Surabhi Narayan'" 
              value={query}
              onChange={(e) => setQuery(e.target.value)}
            />
            <button type="submit" disabled={loading}>
              {loading ? 'Searching...' : 'Search'}
            </button>
          </div>

          <div className="filters-bar">
            <select value={campus} onChange={(e) => setCampus(e.target.value)} className="dropdown">
              <option value="All">All Campuses</option>
              <option value="RR Campus">RR Campus</option>
              <option value="EC Campus">EC Campus</option>
              <option value="HN Campus">HN Campus</option>
            </select>

            <select value={department} onChange={(e) => setDepartment(e.target.value)} className="dropdown">
              <option value="All">All Departments</option>
              <option value="CSE">Computer Science (CSE)</option>
              <option value="AIML">Artificial Intelligence (AIML)</option>
              <option value="ECE">Electronics (ECE)</option>
              <option value="ME">Mechanical (ME)</option>
            </select>
          </div>
        </form>
      </header>

      <main className="results-container">
        {hasSearched && !loading && professors.length === 0 && (
          <div className="no-results">No professors found matching your criteria. Try loosening your filters!</div>
        )}

        <div className="professor-grid">
          {professors.map((prof, index) => (
            <div className="prof-card clickable-card" key={index} onClick={() => setSelectedProf(prof)}>
              <div className="card-header">
                {prof.image && prof.image.length > 5 ? (
                  <img src={prof.image} alt={prof.name} className="prof-image" />
                ) : (
                  <div className="prof-image-placeholder">{prof.name.charAt(0)}</div>
                )}
                <div>
                  <h2>{prof.name}</h2>
                  <p className="designation">{prof.designation}</p>
                </div>
              </div>
              
              <div className="card-body">
                <p><strong>Campus:</strong> <span className="highlight-tag">{prof.campus || 'N/A'}</span></p>
                <p><strong>Department:</strong> <span className="highlight-tag">{prof.department || 'N/A'}</span></p>
                <p className="click-hint">Click to view full profile &rarr;</p>
              </div>
            </div>
          ))}
        </div>
      </main>
    </div>
  )
}

export default App