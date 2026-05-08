import React, { useState, useEffect } from 'react';
import { BarChart, Bar, XAxis, YAxis, Tooltip, ResponsiveContainer } from 'recharts';
import './App.css';

const API = {
  identity: 'http://localhost:8081',
  depot: 'http://localhost:8082',
  warehouse: 'http://localhost:8083'
};

const MOCK_STOCK = [
  { id: 1, sku: 'SKU-100', name: 'Hammer', quantity: 50 },
  { id: 2, sku: 'SKU-200', name: 'Cordless Drill', quantity: 35 },
  { id: 3, sku: 'SKU-300', name: 'Circular Saw', quantity: 20 },
  { id: 4, sku: 'SKU-400', name: 'Angle Grinder', quantity: 8 },
  { id: 5, sku: 'SKU-500', name: 'Impact Driver', quantity: 12 }
];

const MOCK_ASSETS = [
  { tag: 'ASSET-1001', model: 'Hilti TE 7-C', status: 'OUT_WITH_TECH' },
  { tag: 'ASSET-1002', model: 'DeWalt DCD996', status: 'IN_DEPOT' },
  { tag: 'ASSET-1003', model: 'Bosch GSB 18V', status: 'OUT_WITH_TECH' },
  { tag: 'ASSET-1004', model: 'Makita DHR243', status: 'IN_DEPOT' },
  { tag: 'ASSET-1005', model: 'Festool TSC 55', status: 'IN_DEPOT' }
];

const MOCK_FREQUENCY = [
  { model: 'Cordless Drill', count: 32 },
  { model: 'Circular Saw', count: 22 },
  { model: 'Hammer', count: 14 },
  { model: 'Impact Driver', count: 9 },
  { model: 'Angle Grinder', count: 6 }
];

const ACTIVITY_DATA = [
  { day: 'Mon', checkouts: 4 },
  { day: 'Tue', checkouts: 7 },
  { day: 'Wed', checkouts: 3 },
  { day: 'Thu', checkouts: 9 },
  { day: 'Fri', checkouts: 6 },
  { day: 'Sat', checkouts: 2 },
  { day: 'Sun', checkouts: 5 }
];

function StatusBadge({ status }) {
  if (status === 'OUT_WITH_TECH') return <span className="badge badge-red">OUT</span>;
  if (status === 'IN_TRANSIT') return <span className="badge badge-yellow">TRANSIT</span>;
  if (status === 'OVERDUE') return <span className="badge badge-red">OVERDUE</span>;
  if (status === 'COMPLETED') return <span className="badge badge-blue">COMPLETED</span>;
  if (status === 'ACTIVE') return <span className="badge badge-green">ACTIVE</span>;
  return <span className="badge badge-green">IN DEPOT</span>;
}

function Toast({ message, visible }) {
  return <div className={`toast ${visible ? 'toast-visible' : ''}`}>✓ {message}</div>;
}

function StockBar({ name, quantity, max }) {
  const pct = Math.round((quantity / max) * 100);
  const color = quantity < 10 ? '#ef4444' : quantity < 20 ? '#eab308' : '#3b82f6';
  return (
    <div className="stock-bar-wrap">
      <div className="stock-bar-top">
        <span className="stock-bar-name">{name}</span>
        <span className="stock-bar-count">{quantity} units</span>
      </div>
      <div className="stock-bar-track">
        <div className="stock-bar-fill" style={{ width: `${pct}%`, background: color }} />
      </div>
    </div>
  );
}

function TechnicianView({ token, userName, onLogout }) {
  const [jobs, setJobs] = useState([]);
  const [loading, setLoading] = useState(true);
  const [toast, setToast] = useState({ visible: false, message: '' });

  const showToast = (msg) => {
    setToast({ visible: true, message: msg });
    setTimeout(() => setToast({ visible: false, message: '' }), 2500);
  };

  useEffect(() => {
    const load = async () => {
      try {
        const r = await fetch(`${API.depot}/jobs/my-jobs`, {
          headers: { Authorization: `Bearer ${token}` }
        });
        if (r.ok) setJobs(await r.json());
      } catch (e) {}
      setLoading(false);
    };
    load();
  }, [token]);

  const markComplete = async (jobId, jobName) => {
    try {
      const r = await fetch(`${API.depot}/jobs/${jobId}/complete`, {
        method: 'POST',
        headers: { Authorization: `Bearer ${token}` }
      });
      if (r.ok) {
        setJobs(prev => prev.filter(j => j.id !== jobId));
        showToast(`${jobName} marked complete`);
      }
    } catch (e) {}
  };

  return (
    <div className="app">
      <div className="sidebar">
        <div className="logo">
          <div className="logo-mark">
            <div className="logo-icon">TV</div>
            <div>
              <div className="logo-text">ToolVault</div>
              <div className="logo-sub">Field View</div>
            </div>
          </div>
        </div>
        <div className="sidebar-footer" style={{ marginTop: 'auto' }}>
          <div className="user-row">
            <div className="avatar">{userName.slice(0, 2).toUpperCase()}</div>
            <div>
              <div className="user-name">{userName}</div>
              <div className="user-role">TECHNICIAN</div>
            </div>
          </div>
          <button className="btn" style={{ width: '100%', marginTop: 8 }} onClick={onLogout}>Sign out</button>
        </div>
      </div>
      <div className="main">
        <div className="topbar">
          <div>
            <div className="page-title">My Jobs</div>
            <div className="page-sub">Your active assignments</div>
          </div>
          <div className="topbar-right">
            <div className="status-dot" style={{ background: '#22c55e' }} />
            <div className="status-text">Live</div>
          </div>
        </div>
        <div className="content">
          {loading ? (
            <div style={{ color: '#64748b', fontSize: 13 }}>Loading your jobs...</div>
          ) : jobs.length === 0 ? (
            <div className="card" style={{ textAlign: 'center', padding: 40 }}>
              <div style={{ fontSize: 32, marginBottom: 12 }}>✓</div>
              <div style={{ fontSize: 14, color: '#e2e8f0', fontWeight: 500 }}>No active jobs</div>
              <div style={{ fontSize: 12, color: '#64748b', marginTop: 4 }}>All caught up — check with your manager for new assignments</div>
            </div>
          ) : (
            jobs.map(job => (
              <div key={job.id} className="card" style={{ marginBottom: 12 }}>
                <div className="card-header">
                  <div>
                    <div className="card-title">{job.name}</div>
                    <div className="card-sub">{job.site}</div>
                  </div>
                  <StatusBadge status={job.status} />
                </div>
                {job.assetTags && job.assetTags.length > 0 && (
                  <div style={{ marginBottom: 12 }}>
                    <div style={{ fontSize: 11, color: '#64748b', marginBottom: 6 }}>ASSIGNED TOOLS</div>
                    {job.assetTags.map(tag => (
                      <div key={tag} className="asset-row">
                        <div className="asset-tag">{tag}</div>
                        <span className="badge badge-red">OUT</span>
                      </div>
                    ))}
                  </div>
                )}
                <button
                  className="btn btn-primary"
                  style={{ width: '100%' }}
                  onClick={() => markComplete(job.id, job.name)}
                >
                  Mark job complete
                </button>
              </div>
            ))
          )}
        </div>
      </div>
      <Toast message={toast.message} visible={toast.visible} />
    </div>
  );
}

function ManagerSidebar({ activePage, setActivePage, alertCount }) {
  const navItems = [
    { id: 'dashboard', label: 'Dashboard', section: 'Overview' },
    { id: 'assets', label: 'Assets', section: 'Inventory' },
    { id: 'stock', label: 'Stock & Registry', section: null },
    { id: 'checkout', label: 'Check in / out', section: null },
    { id: 'jobs', label: 'Jobs', section: 'Operations' },
    { id: 'forecast', label: 'Forecasting', section: 'Intelligence' },
    { id: 'alerts', label: 'Alerts', section: null, badge: alertCount }
  ];
  let lastSection = null;
  return (
    <div className="sidebar">
      <div className="logo">
        <div className="logo-mark">
          <div className="logo-icon">TV</div>
          <div>
            <div className="logo-text">ToolVault</div>
            <div className="logo-sub">v0.1.0</div>
          </div>
        </div>
      </div>
      <div className="nav">
        {navItems.map(item => {
          const showSection = item.section && item.section !== lastSection;
          if (item.section) lastSection = item.section;
          return (
            <React.Fragment key={item.id}>
              {showSection && <div className="nav-section">{item.section}</div>}
              <div
                className={`nav-item ${activePage === item.id ? 'active' : ''}`}
                onClick={() => setActivePage(item.id)}
              >
                {item.label}
                {item.badge > 0 && <span className="nav-badge">{item.badge}</span>}
              </div>
            </React.Fragment>
          );
        })}
      </div>
    </div>
  );
}

function DashboardPage({ stock, assets, setActivePage }) {
  const inDepot = assets.filter(a => a.status === 'IN_DEPOT').length;
  const outCount = assets.filter(a => a.status !== 'IN_DEPOT').length;
  const lowStock = stock.filter(s => s.quantity < 15).length;
  const max = Math.max(...stock.map(s => s.quantity), 1);
  return (
    <div>
      <div className="grid-4">
        <div className="metric-card metric-clickable" onClick={() => setActivePage('assets')}>
          <div className="metric-label">Total assets</div>
          <div className="metric-value">{assets.length}</div>
          <div className="metric-delta delta-up">All tracked</div>
        </div>
        <div className="metric-card metric-clickable" onClick={() => setActivePage('assets')}>
          <div className="metric-label">In depot</div>
          <div className="metric-value">{inDepot}</div>
          <div className="metric-delta delta-up">Available</div>
        </div>
        <div className="metric-card metric-clickable" onClick={() => setActivePage('checkout')}>
          <div className="metric-label">Checked out</div>
          <div className="metric-value">{outCount}</div>
          <div className="metric-delta" style={{ color: '#94a3b8' }}>With techs</div>
        </div>
        <div className="metric-card metric-clickable" onClick={() => setActivePage('stock')}>
          <div className="metric-label">Low stock</div>
          <div className="metric-value" style={{ color: '#f87171' }}>{lowStock}</div>
          <div className="metric-delta delta-down">Needs reorder</div>
        </div>
      </div>
      <div className="grid-2">
        <div className="card">
          <div className="card-header">
            <div><div className="card-title">Stock levels</div><div className="card-sub">Current warehouse quantities</div></div>
          </div>
          {stock.slice(0, 4).map(s => <StockBar key={s.sku} name={s.name} quantity={s.quantity} max={max} />)}
        </div>
        <div className="card">
          <div className="card-header">
            <div><div className="card-title">Checkout activity</div><div className="card-sub">Last 7 days</div></div>
          </div>
          <ResponsiveContainer width="100%" height={180}>
            <BarChart data={ACTIVITY_DATA}>
              <XAxis dataKey="day" tick={{ fill: '#64748b', fontSize: 11 }} axisLine={false} tickLine={false} />
              <YAxis tick={{ fill: '#64748b', fontSize: 11 }} axisLine={false} tickLine={false} />
              <Tooltip contentStyle={{ background: '#1e293b', border: 'none', borderRadius: 6, color: '#e2e8f0', fontSize: 12 }} />
              <Bar dataKey="checkouts" fill="#3b82f6" radius={[3, 3, 0, 0]} />
            </BarChart>
          </ResponsiveContainer>
        </div>
      </div>
      <div className="card">
        <div className="card-header">
          <div><div className="card-title">Recent assets</div><div className="card-sub">Latest tracked equipment</div></div>
          <span className="badge badge-blue">Live</span>
        </div>
        {assets.slice(0, 4).map(a => (
          <div key={a.tag} className="asset-row">
            <div className="asset-tag">{a.tag}</div>
            <div className="asset-model">{a.model}</div>
            <StatusBadge status={a.status} />
            <div className="asset-site">{a.status === 'OUT_WITH_TECH' ? 'Site' : 'Depot'}</div>
          </div>
        ))}
      </div>
    </div>
  );
}

function AssetsPage({ assets }) {
  return (
    <div className="card">
      <div className="card-header">
        <div><div className="card-title">All assets</div><div className="card-sub">Full equipment register</div></div>
        <button className="btn btn-primary" style={{ fontSize: 11 }}>+ Add asset</button>
      </div>
      {assets.map(a => (
        <div key={a.tag} className="asset-row">
          <div className="asset-tag">{a.tag}</div>
          <div className="asset-model">{a.model}</div>
          <StatusBadge status={a.status} />
          <div className="asset-site">{a.status === 'OUT_WITH_TECH' ? 'Site' : 'Depot'}</div>
        </div>
      ))}
    </div>
  );
}

function StockPage({ stock, assets, jobs }) {
  const [selected, setSelected] = useState(null);
  const max = Math.max(...stock.map(s => s.quantity), 1);
  const sorted = [...stock].sort((a, b) => b.quantity - a.quantity).slice(0, 5);

  const getAssetDetails = (stockItem) => {
    const matchingAssets = assets.filter(a =>
      a.model && stockItem.name &&
      a.model.toLowerCase().includes(stockItem.name.toLowerCase().split(' ')[0])
    );
    return matchingAssets.map(asset => {
      const job = jobs.find(j => j.assetTags && j.assetTags.includes(asset.tag));
      return { ...asset, job };
    });
  };

  return (
    <div>
      <div className="grid-2">
        <div className="card">
          <div className="card-header">
            <div><div className="card-title">Warehouse stock</div><div className="card-sub">Click a SKU to see tool details</div></div>
          </div>
          {stock.map(s => (
            <div
              key={s.sku}
              onClick={() => setSelected(selected?.sku === s.sku ? null : s)}
              style={{ cursor: 'pointer', borderRadius: 6, padding: '4px 0', transition: 'background 0.15s' }}
            >
              <div className="stock-bar-top" style={{ marginBottom: 2 }}>
                <span className="stock-bar-name">{s.name}</span>
                <span style={{ fontSize: 10, color: '#475569', marginRight: 8 }}>{s.sku}</span>
                <span className="stock-bar-count">{s.quantity} units</span>
              </div>
              <div className="stock-bar-track">
                <div className="stock-bar-fill" style={{
                  width: `${Math.round((s.quantity / max) * 100)}%`,
                  background: s.quantity < 10 ? '#ef4444' : s.quantity < 20 ? '#eab308' : '#3b82f6'
                }} />
              </div>
              {selected?.sku === s.sku && (
                <div style={{ marginTop: 10, marginBottom: 4 }}>
                  <div style={{ fontSize: 11, color: '#64748b', marginBottom: 6, textTransform: 'uppercase', letterSpacing: '0.06em' }}>Individual tools</div>
                  {getAssetDetails(s).length === 0 ? (
                    <div style={{ fontSize: 12, color: '#475569', padding: '6px 0' }}>No individual assets tracked for this SKU</div>
                  ) : (
                    getAssetDetails(s).map(asset => (
                      <div key={asset.tag} style={{ display: 'grid', gridTemplateColumns: '100px 1fr auto', gap: 8, padding: '6px 0', borderBottom: '0.5px solid rgba(255,255,255,0.06)', fontSize: 12, alignItems: 'start' }}>
                        <div style={{ color: '#e2e8f0', fontWeight: 500 }}>{asset.tag}</div>
                        <div>
                          <StatusBadge status={asset.status} />
                          {asset.job && (
                            <div style={{ marginTop: 4, color: '#64748b' }}>
                              <div>Job: {asset.job.name}</div>
                              <div>Site: {asset.job.site}</div>
                              <div>Tech: {asset.job.technicianName}</div>
                              {asset.job.returnDeadline && (
                                <div>Return by: {new Date(asset.job.returnDeadline).toLocaleDateString()}</div>
                              )}
                            </div>
                          )}
                        </div>
                        <div style={{ color: '#64748b', fontSize: 11 }}>
                          {asset.status === 'IN_DEPOT' ? 'Available' : asset.status === 'IN_TRANSIT' ? 'Returning' : 'In use'}
                        </div>
                      </div>
                    ))
                  )}
                </div>
              )}
            </div>
          ))}
        </div>
        <div className="card">
          <div className="card-header">
            <div><div className="card-title">Top 5 SKUs by quantity</div><div className="card-sub">Highest stocked items</div></div>
          </div>
          <ResponsiveContainer width="100%" height={220}>
            <BarChart data={sorted.map(s => ({ name: s.name, quantity: s.quantity }))} layout="vertical">
              <XAxis type="number" tick={{ fill: '#64748b', fontSize: 11 }} axisLine={false} tickLine={false} />
              <YAxis type="category" dataKey="name" tick={{ fill: '#94a3b8', fontSize: 11 }} axisLine={false} tickLine={false} width={110} />
              <Tooltip contentStyle={{ background: '#1e293b', border: 'none', borderRadius: 6, color: '#e2e8f0', fontSize: 12 }} />
              <Bar dataKey="quantity" fill="#3b82f6" radius={[0, 3, 3, 0]} />
            </BarChart>
          </ResponsiveContainer>
        </div>
      </div>
    </div>
  );
}

function CheckoutPage({ token, liveMode, showToast, assets, setAssets }) {
  const [coTag, setCoTag] = useState('');
  const [coTech, setCoTech] = useState('');
  const [coSite, setCoSite] = useState('');
  const [coReturn, setCoReturn] = useState('');
  const [ciTag, setCiTag] = useState('');
  const [ciModel, setCiModel] = useState('');
  const [activity, setActivity] = useState([
    { tag: 'ASSET-1001', model: 'Hilti TE 7-C', action: 'OUT', tech: 'J. Martinez', site: 'Site A', returnDate: '2026-05-10' },
    { tag: 'ASSET-1002', model: 'DeWalt DCD996', action: 'IN', tech: '—', site: 'Depot', returnDate: '—' },
    { tag: 'ASSET-1003', model: 'Bosch GSB 18V', action: 'OUT', tech: 'R. Thompson', site: 'Site B', returnDate: '2026-05-08' }
  ]);

  const handleCheckout = async () => {
    if (!coTag) return;
    if (liveMode) {
      try {
        await fetch(`${API.depot}/api/v1/depot/assets/${coTag}/checkout`, {
          method: 'POST',
          headers: { Authorization: `Bearer ${token}`, 'Content-Type': 'application/json' },
          body: JSON.stringify({})
        });
      } catch (e) {}
    }
    const model = assets.find(a => a.tag === coTag)?.model || coTag;
    setAssets(prev => prev.map(a => a.tag === coTag ? { ...a, status: 'OUT_WITH_TECH' } : a));
    setActivity(prev => [{ tag: coTag, model, action: 'OUT', tech: coTech || 'Unknown', site: coSite || '—', returnDate: coReturn || '—' }, ...prev.slice(0, 9)]);
    showToast(`${coTag} checked out to ${coTech || 'technician'}`);
    setCoTag(''); setCoTech(''); setCoSite(''); setCoReturn('');
  };

  const handleCheckin = async () => {
    if (!ciTag) return;
    if (liveMode) {
      try {
        await fetch(`${API.depot}/api/v1/depot/assets/${ciTag}/checkin`, {
          method: 'POST',
          headers: { Authorization: `Bearer ${token}`, 'Content-Type': 'application/json' },
          body: JSON.stringify({ model: ciModel || null })
        });
      } catch (e) {}
    }
    const model = assets.find(a => a.tag === ciTag)?.model || ciModel || ciTag;
    setAssets(prev => prev.map(a => a.tag === ciTag ? { ...a, status: 'IN_DEPOT' } : a));
    setActivity(prev => [{ tag: ciTag, model, action: 'IN', tech: '—', site: 'Depot', returnDate: '—' }, ...prev.slice(0, 9)]);
    showToast(`${ciTag} checked in`);
    setCiTag(''); setCiModel('');
  };

  return (
    <div>
      <div className="grid-2">
        <div className="card">
          <div className="card-header"><div className="card-title">Check out asset</div></div>
          <div className="form-group"><div className="form-label">Asset tag</div><input className="form-input" value={coTag} onChange={e => setCoTag(e.target.value)} placeholder="ASSET-1001" /></div>
          <div className="form-group" style={{ marginTop: 10 }}><div className="form-label">Technician</div><input className="form-input" value={coTech} onChange={e => setCoTech(e.target.value)} placeholder="Tech name or ID" /></div>
          <div className="form-group" style={{ marginTop: 10 }}><div className="form-label">Site</div><input className="form-input" value={coSite} onChange={e => setCoSite(e.target.value)} placeholder="Job site" /></div>
          <div className="form-group" style={{ marginTop: 10 }}><div className="form-label">Anticipated return date</div><input className="form-input" type="date" value={coReturn} onChange={e => setCoReturn(e.target.value)} /></div>
          <button className="btn btn-primary" style={{ marginTop: 14, width: '100%' }} onClick={handleCheckout}>Check out</button>
        </div>
        <div className="card">
          <div className="card-header"><div className="card-title">Check in asset</div></div>
          <div className="form-group"><div className="form-label">Asset tag</div><input className="form-input" value={ciTag} onChange={e => setCiTag(e.target.value)} placeholder="ASSET-1001" /></div>
          <div className="form-group" style={{ marginTop: 10 }}><div className="form-label">Model (optional)</div><input className="form-input" value={ciModel} onChange={e => setCiModel(e.target.value)} placeholder="e.g. Hilti TE 7-C" /></div>
          <button className="btn" style={{ marginTop: 14, width: '100%' }} onClick={handleCheckin}>Check in</button>
        </div>
      </div>
      <div className="card">
        <div className="card-header"><div className="card-title">Recent activity</div></div>
        <div style={{ overflowX: 'auto' }}>
          <table style={{ width: '100%', borderCollapse: 'collapse', fontSize: 12 }}>
            <thead>
              <tr style={{ borderBottom: '0.5px solid rgba(255,255,255,0.08)' }}>
                {['Tag', 'Model', 'Status', 'Technician', 'Site', 'Return date'].map(h => (
                  <th key={h} style={{ textAlign: 'left', padding: '6px 8px', color: '#64748b', fontWeight: 500 }}>{h}</th>
                ))}
              </tr>
            </thead>
            <tbody>
              {activity.map((a, i) => (
                <tr key={i} style={{ borderBottom: '0.5px solid rgba(255,255,255,0.06)' }}>
                  <td style={{ padding: '8px', color: '#e2e8f0', fontWeight: 500 }}>{a.tag}</td>
                  <td style={{ padding: '8px', color: '#94a3b8' }}>{a.model}</td>
                  <td style={{ padding: '8px' }}>{a.action === 'OUT' ? <span className="badge badge-red">OUT</span> : <span className="badge badge-green">IN</span>}</td>
                  <td style={{ padding: '8px', color: '#94a3b8' }}>{a.tech}</td>
                  <td style={{ padding: '8px', color: '#64748b' }}>{a.site}</td>
                  <td style={{ padding: '8px', color: '#64748b' }}>{a.returnDate}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}

function JobsPage({ token, showToast }) {
  const [jobs, setJobs] = useState([]);
  const [assets, setAssets] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showCreate, setShowCreate] = useState(false);
  const [form, setForm] = useState({ name: '', site: '', technicianEmail: '', technicianName: '' });
  const [assignTag, setAssignTag] = useState('');
  const [selectedJob, setSelectedJob] = useState(null);
  const [statusFilter, setStatusFilter] = useState('ALL');

  const load = async () => {
    try {
      const [jr, ar] = await Promise.all([
        fetch(`${API.depot}/jobs`, { headers: { Authorization: `Bearer ${token}` } }),
        fetch(`${API.depot}/api/v1/depot/assets`, { headers: { Authorization: `Bearer ${token}` } })
      ]);
      if (jr.ok) setJobs(await jr.json());
      if (ar.ok) setAssets(await ar.json());
    } catch (e) {}
    setLoading(false);
  };

  useEffect(() => { load(); }, [token]);

  const createJob = async () => {
    if (!form.name || !form.site || !form.technicianEmail || !form.technicianName) return;
    try {
      const r = await fetch(`${API.depot}/jobs`, {
        method: 'POST',
        headers: { Authorization: `Bearer ${token}`, 'Content-Type': 'application/json' },
        body: JSON.stringify(form)
      });
      if (r.ok) {
        await load();
        setShowCreate(false);
        setForm({ name: '', site: '', technicianEmail: '', technicianName: '' });
        showToast('Job created');
      }
    } catch (e) {}
  };

  const assignAsset = async (jobId) => {
    if (!assignTag) return;
    try {
      const r = await fetch(`${API.depot}/jobs/${jobId}/assign-asset`, {
        method: 'POST',
        headers: { Authorization: `Bearer ${token}`, 'Content-Type': 'application/json' },
        body: JSON.stringify({ assetTag: assignTag })
      });
      if (r.ok) {
        await load();
        setAssignTag('');
        showToast(`${assignTag} assigned`);
      }
    } catch (e) {}
  };

  const filtered = statusFilter === 'ALL' ? jobs : jobs.filter(j => j.status === statusFilter);

  const statusColor = (s) => {
    if (s === 'ACTIVE') return '#22c55e';
    if (s === 'COMPLETED') return '#3b82f6';
    if (s === 'OVERDUE') return '#ef4444';
    return '#64748b';
  };

  if (loading) return <div style={{ color: '#64748b', fontSize: 13 }}>Loading jobs...</div>;

  return (
    <div>
      <div style={{ display: 'flex', gap: 8, marginBottom: 16, alignItems: 'center' }}>
        <div className="tab-row" style={{ marginBottom: 0, flex: 1 }}>
          {['ALL', 'ACTIVE', 'COMPLETED', 'OVERDUE'].map(s => (
            <button key={s} className={`tab ${statusFilter === s ? 'active' : ''}`} onClick={() => setStatusFilter(s)}>{s}</button>
          ))}
        </div>
        <button className="btn btn-primary" onClick={() => setShowCreate(!showCreate)}>+ New job</button>
      </div>

      {showCreate && (
        <div className="card" style={{ marginBottom: 16 }}>
          <div className="card-header"><div className="card-title">Create new job</div></div>
          <div className="grid-2">
            <div className="form-group"><div className="form-label">Job name</div><input className="form-input" value={form.name} onChange={e => setForm({ ...form, name: e.target.value })} placeholder="Electrical Inspection" /></div>
            <div className="form-group"><div className="form-label">Site</div><input className="form-input" value={form.site} onChange={e => setForm({ ...form, site: e.target.value })} placeholder="Site A" /></div>
            <div className="form-group"><div className="form-label">Technician name</div><input className="form-input" value={form.technicianName} onChange={e => setForm({ ...form, technicianName: e.target.value })} placeholder="John Smith" /></div>
            <div className="form-group"><div className="form-label">Technician email</div><input className="form-input" value={form.technicianEmail} onChange={e => setForm({ ...form, technicianEmail: e.target.value })} placeholder="employee@toolvault.local" /></div>
          </div>
          <div style={{ display: 'flex', gap: 8, marginTop: 12 }}>
            <button className="btn btn-primary" onClick={createJob}>Create job</button>
            <button className="btn" onClick={() => setShowCreate(false)}>Cancel</button>
          </div>
        </div>
      )}

      {filtered.length === 0 ? (
        <div className="card" style={{ textAlign: 'center', padding: 32, color: '#64748b', fontSize: 13 }}>No jobs found</div>
      ) : (
        filtered.map(job => (
          <div key={job.id} className="card" style={{ marginBottom: 12 }}>
            <div className="card-header">
              <div>
                <div className="card-title">{job.name}</div>
                <div className="card-sub">{job.site} · {job.technicianName}</div>
              </div>
              <div style={{ display: 'flex', gap: 8, alignItems: 'center' }}>
                <span style={{ fontSize: 11, fontWeight: 500, color: statusColor(job.status), background: `${statusColor(job.status)}20`, padding: '2px 8px', borderRadius: 10 }}>{job.status}</span>
                <button className="btn" style={{ fontSize: 11 }} onClick={() => setSelectedJob(selectedJob?.id === job.id ? null : job)}>
                  {selectedJob?.id === job.id ? 'Close' : 'Manage'}
                </button>
              </div>
            </div>

            {job.assetTags && job.assetTags.length > 0 && (
              <div style={{ marginBottom: 8 }}>
                <div style={{ fontSize: 11, color: '#64748b', marginBottom: 4 }}>ASSIGNED TOOLS</div>
                <div style={{ display: 'flex', gap: 6, flexWrap: 'wrap' }}>
                  {job.assetTags.map(tag => <span key={tag} className="badge badge-blue">{tag}</span>)}
                </div>
              </div>
            )}

            {job.returnDeadline && (
              <div style={{ fontSize: 11, color: '#64748b', marginTop: 4 }}>
                Return deadline: {new Date(job.returnDeadline).toLocaleString()}
              </div>
            )}

            {selectedJob?.id === job.id && job.status === 'ACTIVE' && (
              <div style={{ marginTop: 12, paddingTop: 12, borderTop: '0.5px solid rgba(255,255,255,0.08)' }}>
                <div style={{ fontSize: 11, color: '#64748b', marginBottom: 8 }}>ASSIGN ASSET</div>
                <div style={{ display: 'flex', gap: 8 }}>
                  <select className="form-select" value={assignTag} onChange={e => setAssignTag(e.target.value)} style={{ flex: 1 }}>
                    <option value="">Select available asset...</option>
                    {assets.filter(a => a.status === 'IN_DEPOT').map(a => (
                      <option key={a.tag} value={a.tag}>{a.tag} — {a.model}</option>
                    ))}
                  </select>
                  <button className="btn btn-primary" onClick={() => assignAsset(job.id)}>Assign</button>
                </div>
              </div>
            )}
          </div>
        ))
      )}
    </div>
  );
}

function ForecastPage({ token, liveMode, frequency }) {
  const [risk, setRisk] = useState([]);
  const [reorder, setReorder] = useState([]);
  const [loaded, setLoaded] = useState(false);

  useEffect(() => {
    if (!liveMode || loaded) return;
    const load = async () => {
      try {
        const [rr, ro] = await Promise.all([
          fetch(`${API.depot}/forecasting/risk`, { headers: { Authorization: `Bearer ${token}` } }),
          fetch(`${API.depot}/forecasting/reorder`, { headers: { Authorization: `Bearer ${token}` } })
        ]);
        if (rr.ok) setRisk(await rr.json());
        if (ro.ok) {
          const data = await ro.json();
          setReorder(Object.entries(data).map(([model, s]) => ({ model, ...s })));
        }
      } catch (e) {}
      setLoaded(true);
    };
    load();
  }, [token, liveMode, loaded]);

  const mockStockout = [
    { model: 'Cordless Drill', rate: '8.2 checkouts/week', days: 4, color: '#f87171' },
    { model: 'Circular Saw', rate: '5.1 checkouts/week', days: 11, color: '#facc15' },
    { model: 'Hammer', rate: '3.4 checkouts/week', days: 28, color: '#4ade80' }
  ];

  return (
    <div>
      <div className="grid-2">
        <div className="card">
          <div className="card-header">
            <div><div className="card-title">Checkout frequency</div><div className="card-sub">Checkouts per model — last 30 days</div></div>
          </div>
          <ResponsiveContainer width="100%" height={220}>
            <BarChart data={frequency}>
              <XAxis dataKey="model" tick={{ fill: '#64748b', fontSize: 10 }} axisLine={false} tickLine={false} />
              <YAxis tick={{ fill: '#64748b', fontSize: 11 }} axisLine={false} tickLine={false} />
              <Tooltip contentStyle={{ background: '#1e293b', border: 'none', borderRadius: 6, color: '#e2e8f0', fontSize: 12 }} />
              <Bar dataKey="count" fill="#6366f1" radius={[3, 3, 0, 0]} />
            </BarChart>
          </ResponsiveContainer>
        </div>
        <div className="card">
          <div className="card-header">
            <div><div className="card-title">Days until stockout</div><div className="card-sub">At current usage rate</div></div>
          </div>
          {mockStockout.map(s => (
            <div key={s.model} className="forecast-item">
              <div>
                <div className="forecast-model">{s.model}</div>
                <div className="forecast-rate">{s.rate}</div>
              </div>
              <div style={{ textAlign: 'right' }}>
                <div className="forecast-days" style={{ color: s.color }}>{s.days}</div>
                <div className="forecast-label">days remaining</div>
              </div>
            </div>
          ))}
        </div>
      </div>

      {risk.length > 0 && (
        <div className="card" style={{ marginBottom: 16 }}>
          <div className="card-header">
            <div><div className="card-title">At-risk models</div><div className="card-sub">Checkouts significantly outpacing returns</div></div>
            <span className="badge badge-red">{risk.length} flagged</span>
          </div>
          <div style={{ display: 'flex', gap: 6, flexWrap: 'wrap' }}>
            {risk.map(r => <span key={r} className="badge badge-red">{r}</span>)}
          </div>
        </div>
      )}

      <div className="card">
        <div className="card-header">
          <div>
            <div className="card-title">Reorder suggestions</div>
            <div className="card-sub">
              {liveMode && reorder.length > 0
                ? `AI-generated — based on ${reorder[0]?.analysisWindowDays || 30}-day usage, targeting ${reorder[0]?.coverageDays || 30} days coverage`
                : 'AI-generated recommendations based on usage trends'}
            </div>
          </div>
        </div>
        {reorder.length > 0 ? (
          reorder.map(r => (
            <div key={r.model} className="reorder-row">
              <div>
                <div className="reorder-sku">{r.model}</div>
                <div style={{ fontSize: 11, color: '#64748b', marginTop: 2 }}>
                  {r.dailyUsageRate?.toFixed(2)} checkouts/day · {r.coverageDays}-day coverage target
                </div>
              </div>
              <div style={{ textAlign: 'right' }}>
                <div className="reorder-qty">Order {r.suggestedQuantity} units</div>
                <span className="badge badge-red" style={{ marginTop: 4, display: 'block' }}>Urgent</span>
              </div>
            </div>
          ))
        ) : (
          <>
            <div className="reorder-row">
              <div><div className="reorder-sku">SKU-200 — Cordless Drill</div><div style={{ fontSize: 11, color: '#64748b', marginTop: 2 }}>High demand, low return rate</div></div>
              <div style={{ textAlign: 'right' }}><div className="reorder-qty">Order 16 units</div><span className="badge badge-red" style={{ marginTop: 4, display: 'block' }}>Urgent</span></div>
            </div>
            <div className="reorder-row">
              <div><div className="reorder-sku">SKU-300 — Circular Saw</div><div style={{ fontSize: 11, color: '#64748b', marginTop: 2 }}>Moderate risk, trending up</div></div>
              <div style={{ textAlign: 'right' }}><div className="reorder-qty">Order 10 units</div><span className="badge badge-yellow" style={{ marginTop: 4, display: 'block' }}>Soon</span></div>
            </div>
          </>
        )}
      </div>
    </div>
  );
}

function AlertsPage({ token, liveMode, onAlertResolved }) {
  const [alerts, setAlerts] = useState([]);
  const [loading, setLoading] = useState(true);

  const load = async () => {
    if (!liveMode) { setLoading(false); return; }
    try {
      const r = await fetch(`${API.depot}/jobs/alerts`, { headers: { Authorization: `Bearer ${token}` } });
      if (r.ok) setAlerts(await r.json());
    } catch (e) {}
    setLoading(false);
  };

  useEffect(() => { load(); }, [token, liveMode]);

  const resolve = async (alertId) => {
    try {
      const r = await fetch(`${API.depot}/jobs/alerts/${alertId}/resolve`, {
        method: 'POST',
        headers: { Authorization: `Bearer ${token}` }
      });
      if (r.ok) {
        setAlerts(prev => prev.filter(a => a.id !== alertId));
        onAlertResolved();
      }
    } catch (e) {}
  };

  const typeColor = (type) => {
    if (type === 'OVERDUE_RETURN') return 'alert-icon-danger';
    if (type === 'JOB_COMPLETED') return 'alert-icon-warn';
    return 'alert-icon-warn';
  };

  const mockAlerts = [
    { id: 'm1', type: 'OVERDUE_RETURN', jobName: 'Electrical Inspection', technicianName: 'John Smith', site: 'Site A', message: 'Tools not returned — deadline passed', createdAt: new Date().toISOString() },
    { id: 'm2', type: 'JOB_COMPLETED', jobName: 'Plumbing Inspection', technicianName: 'John Smith', site: 'Site B', message: 'Job marked complete. Tools must be returned within 24 hours.', createdAt: new Date().toISOString() }
  ];

  const displayAlerts = liveMode ? alerts : mockAlerts;

  return (
    <div>
      <div className="card">
        <div className="card-header">
          <div><div className="card-title">Active alerts</div><div className="card-sub">Items requiring attention</div></div>
          <span className="badge badge-red">{displayAlerts.length} active</span>
        </div>
        {loading ? (
          <div style={{ color: '#64748b', fontSize: 13, padding: '12px 0' }}>Loading alerts...</div>
        ) : displayAlerts.length === 0 ? (
          <div style={{ textAlign: 'center', padding: 32, color: '#64748b', fontSize: 13 }}>No active alerts — all clear ✓</div>
        ) : (
          displayAlerts.map(alert => (
            <div key={alert.id} className="alert-row">
              <div className={`alert-icon ${typeColor(alert.type)}`}>⚠</div>
              <div className="alert-text">
                <div className="alert-title">
                  {alert.jobName} — {alert.type === 'OVERDUE_RETURN' ? 'Overdue return' : 'Job completed'}
                </div>
                <div className="alert-desc">
                  {alert.technicianName} · {alert.site} · {alert.message}
                </div>
                <div style={{ fontSize: 10, color: '#475569', marginTop: 2 }}>
                  {new Date(alert.createdAt).toLocaleString()}
                </div>
              </div>
              {liveMode && (
                <button className="btn" style={{ fontSize: 11, flexShrink: 0 }} onClick={() => resolve(alert.id)}>
                  Resolve
                </button>
              )}
            </div>
          ))
        )}
      </div>
    </div>
  );
}

function LoginScreen({ onLogin }) {
  const [email, setEmail] = useState('employee@toolvault.local');
  const [password, setPassword] = useState('Employee123!');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleLogin = async () => {
    setLoading(true);
    setError('');
    try {
      const res = await fetch(`${API.identity}/auth/login`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email, password })
      });
      if (res.ok) {
        const data = await res.json();
        onLogin({ token: data.accessToken, email, live: true });
        return;
      }
    } catch (e) {}
    if (email === 'employee@toolvault.local' && password === 'Employee123!') {
      onLogin({ token: null, email, live: false, role: 'USER' });
    } else if (email === 'manager@toolvault.local' && password === 'Manager123!') {
      onLogin({ token: null, email, live: false, role: 'ADMIN' });
    } else {
      setError('Invalid credentials — check your email and password');
    }
    setLoading(false);
  };

  return (
    <div className="login-wrap">
      <div className="login-card">
        <div className="login-logo">
          <div className="logo-icon">TV</div>
          <div><div className="logo-text">ToolVault</div><div className="logo-sub">Inventory Management</div></div>
        </div>
        <div className="login-title">Welcome back</div>
        <div className="login-sub">Sign in to your ToolVault workspace</div>
        <div className="login-form">
          <input className="login-input" type="email" value={email} onChange={e => setEmail(e.target.value)} placeholder="Email address" onKeyDown={e => e.key === 'Enter' && handleLogin()} />
          <input className="login-input" type="password" value={password} onChange={e => setPassword(e.target.value)} placeholder="Password" onKeyDown={e => e.key === 'Enter' && handleLogin()} />
          {error && <div className="login-err">{error}</div>}
          <button className="login-btn" onClick={handleLogin} disabled={loading}>{loading ? 'Signing in...' : 'Sign in'}</button>
        </div>
        <div className="login-footer">Manager: manager@toolvault.local / Manager123!</div>
        <div className="login-footer" style={{ marginTop: 4 }}>Technician: employee@toolvault.local / Employee123!</div>
      </div>
    </div>
  );
}

export default function App() {
  const [authed, setAuthed] = useState(false);
  const [token, setToken] = useState(null);
  const [liveMode, setLiveMode] = useState(false);
  const [userRole, setUserRole] = useState('USER');
  const [userName, setUserName] = useState('');
  const [activePage, setActivePage] = useState('dashboard');
  const [stock, setStock] = useState(MOCK_STOCK);
  const [assets, setAssets] = useState(MOCK_ASSETS);
  const [jobs, setJobs] = useState([]);
  const [frequency, setFrequency] = useState(MOCK_FREQUENCY);
  const [alertCount, setAlertCount] = useState(0);
  const [toast, setToast] = useState({ visible: false, message: '' });

  const pageTitles = {
    dashboard: { title: 'Dashboard', sub: 'Overview of your inventory system' },
    assets: { title: 'Assets', sub: 'Track and manage all equipment' },
    stock: { title: 'Stock & Registry', sub: 'Warehouse quantities and tool status' },
    checkout: { title: 'Check in / out', sub: 'Asset movement management' },
    jobs: { title: 'Jobs', sub: 'Manage field assignments and tool returns' },
    forecast: { title: 'Forecasting', sub: 'AI-powered inventory intelligence' },
    alerts: { title: 'Alerts', sub: 'Items requiring immediate attention' }
  };

  const showToast = (message) => {
    setToast({ visible: true, message });
    setTimeout(() => setToast({ visible: false, message: '' }), 2500);
  };

  const refreshAlertCount = async (tok) => {
    try {
      const r = await fetch(`${API.depot}/jobs/alerts/count`, { headers: { Authorization: `Bearer ${tok}` } });
      if (r.ok) { const d = await r.json(); setAlertCount(d.count || 0); }
    } catch (e) {}
  };

  const handleLogin = async ({ token, email, live, role }) => {
    setToken(token);
    setLiveMode(live);
    setUserRole(role || (email.includes('manager') ? 'ADMIN' : 'USER'));
    setUserName(email.includes('manager') ? 'Manager' : 'Employee');
    setAuthed(true);

    if (live && token) {
      try {
        const [sr, ar, jr, fr] = await Promise.all([
          fetch(`${API.warehouse}/warehouse/stock`, { headers: { Authorization: `Bearer ${token}` } }),
          fetch(`${API.depot}/api/v1/depot/assets`, { headers: { Authorization: `Bearer ${token}` } }),
          fetch(`${API.depot}/jobs`, { headers: { Authorization: `Bearer ${token}` } }),
          fetch(`${API.depot}/forecasting/frequency`, { headers: { Authorization: `Bearer ${token}` } })
        ]);
        if (sr.ok) setStock(await sr.json());
        if (ar.ok) setAssets(await ar.json());
        if (jr.ok) setJobs(await jr.json());
        if (fr.ok) {
          const data = await fr.json();
          const mapped = Object.entries(data).map(([model, count]) => ({ model, count }));
          if (mapped.length > 0) setFrequency(mapped);
        }
      } catch (e) {}
      refreshAlertCount(token);
    }
  };

  const handleLogout = () => {
    setAuthed(false); setToken(null); setLiveMode(false);
    setActivePage('dashboard'); setStock(MOCK_STOCK);
    setAssets(MOCK_ASSETS); setJobs([]); setFrequency(MOCK_FREQUENCY);
    setAlertCount(0); setUserRole('USER');
  };

  if (!authed) return <LoginScreen onLogin={handleLogin} />;

  if (userRole === 'USER') {
    return <TechnicianView token={token} userName={userName} onLogout={handleLogout} />;
  }

  const { title, sub } = pageTitles[activePage] || pageTitles.dashboard;

  return (
    <div className="app">
      <ManagerSidebar activePage={activePage} setActivePage={setActivePage} alertCount={alertCount} />
      <div className="main">
        <div className="topbar">
          <div>
            <div className="page-title">{title}</div>
            <div className="page-sub">{sub}</div>
          </div>
          <div className="topbar-right">
            <div className="status-dot" style={{ background: liveMode ? '#22c55e' : '#eab308' }} />
            <div className="status-text">{liveMode ? 'Live' : 'Demo mode'}</div>
            <div className="user-name" style={{ fontSize: 12, color: '#94a3b8', marginLeft: 4 }}>{userName}</div>
            <button className="btn" onClick={handleLogout}>Sign out</button>
          </div>
        </div>
        {!liveMode && <div className="api-notice">⚠ Using demo data — start Docker backend to connect live data</div>}
        <div className="content">
          {activePage === 'dashboard' && <DashboardPage stock={stock} assets={assets} setActivePage={setActivePage} />}
          {activePage === 'assets' && <AssetsPage assets={assets} />}
          {activePage === 'stock' && <StockPage stock={stock} assets={assets} jobs={jobs} />}
          {activePage === 'checkout' && <CheckoutPage token={token} liveMode={liveMode} showToast={showToast} assets={assets} setAssets={setAssets} />}
          {activePage === 'jobs' && <JobsPage token={token} showToast={showToast} />}
          {activePage === 'forecast' && <ForecastPage token={token} liveMode={liveMode} frequency={frequency} />}
          {activePage === 'alerts' && <AlertsPage token={token} liveMode={liveMode} onAlertResolved={() => refreshAlertCount(token)} />}
        </div>
      </div>
      <Toast message={toast.message} visible={toast.visible} />
    </div>
  );
}